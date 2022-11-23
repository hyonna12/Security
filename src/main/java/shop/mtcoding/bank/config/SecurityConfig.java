package shop.mtcoding.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.handler.LoginHandler;

// SecurityFilterChain
// jwt 필터로 거르고 security 필터 걸러서 ds 들어옴
@Configuration
public class SecurityConfig {

  @Autowired
  private LoginHandler loginHandler;
  // config 에서는 autowired 사용하기

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    // 해쉬로 로그인해야함
    return new BCryptPasswordEncoder();
  }

  @Bean // 리턴되는게 container에 재 등록
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // security 필터를 커스텀해서 사용
    // 다 닫고 필요한 것만 열어주기

    // iframe으로 페이지 여는거 거부
    http.headers().frameOptions().disable();

    // 브라우저에서 로그인폼을 요청해서 응답할 때 특정 값(random hidden key)을 들고오도록 세팅
    // 서버에서 응답해준 페이지로 들어왔는지 확인(페이지에서 요청했는지, 페이지 없이 바로 요청했는지)
    // postman으로 test 하기 위해 해제해줌
    http.csrf().disable();

    // 로그인이 되지 않으면 들어올 수 없음
    http.authorizeHttpRequests()
        .antMatchers("/api/transaction/**").authenticated()
        .antMatchers("/api/user/**").authenticated()
        .antMatchers("/api/account/**").authenticated()
        .antMatchers("/api/admin/**").hasRole("ROLE_" + UserEnum.ADMIN) // 관리자페이지 - 인증 + 권한
        .anyRequest().permitAll() // 이 주소들 제외하고 다 허용
        .and()

        .formLogin() // 디폴트는 x-www-fom-urlencoded (post)
        .usernameParameter("username")
        .passwordParameter("password")
        .loginProcessingUrl("/api/login") // /api/login 으로 가면 스프링 security 로그인폼
        // 기본 디폴트는 user, 제공되는 password 로만 가능 -> 로그인 process 커스텀해줘야함
        .successHandler(loginHandler)
        .failureHandler(loginHandler); // 로그인 성공, 실패 시

    return http.build();
  }
}
