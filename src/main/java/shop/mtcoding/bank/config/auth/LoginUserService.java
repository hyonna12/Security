package shop.mtcoding.bank.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@Service
public class LoginUserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  // user 객체 꺼내주면 검증해줌
  // userdetails를 implement한 user를 넣어서 findById를 넣어주면 검증해서 로그인 됨
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    // LoginUser loginUser = (LoginUser)
    // SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("username을 찾을 수 없습니다."));
    return new LoginUser(user);
  }

}
