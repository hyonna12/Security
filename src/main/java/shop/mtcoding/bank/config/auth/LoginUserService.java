package shop.mtcoding.bank.config.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@Service
public class LoginUserService implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserRepository userRepository;

  // user 객체 꺼내주면 검증해줌
  // userdetails를 implement한 user를 넣어서 findById를 넣어주면 검증해서 로그인 됨
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    // LoginUser loginUser = (LoginUser)
    // SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    log.debug("디버그 : loadUserByUsername 실행됨");
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomApiException("username을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
    return new LoginUser(user);
  }

}
