package shop.mtcoding.bank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.UserReqDto.JoinReqDto;
import shop.mtcoding.bank.dto.UserRespDto.JoinRespDto;

@Transactional(readOnly = true) // lazy loading 하려고
@RequiredArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Transactional
  public JoinRespDto 회원가입(JoinReqDto joinReqDto) {

    log.debug("디버그 : 서비스 회원가입 실행됨");
    // 1. 비밀번호 암호화 / 시큐리티는 password가 무조건 hash
    String rawPassword = joinReqDto.getPassword();
    String encPassword = passwordEncoder.encode(rawPassword);
    joinReqDto.setPassword(encPassword);
    log.debug("디버그 : " + encPassword);

    // 2. 회원가입
    User userPS = userRepository.save(joinReqDto.toEntity());
    log.debug("디버그 : 회원가입 save 완료");

    // 3. DTO 응답
    return new JoinRespDto(userPS);
  }

}
