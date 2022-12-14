package shop.mtcoding.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.config.jwt.JwtAuthenticationFilter;
import shop.mtcoding.bank.config.jwt.JwtAuthorizationFilter;
import shop.mtcoding.bank.util.CustomResponseUtil;

// SecurityFilterChain
// jwt 필터로 거르고 security 필터 걸러서 ds 들어옴
@Configuration
public class SecurityConfig {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    // 해쉬로 로그인해야함
    log.debug("디버그 : passwordEncoder Bean 등록됨");
    return new BCryptPasswordEncoder();
  }

  // 모든 필터 등록은 여기서!!
  public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
      log.debug("디버그 : SecurityConfig의 configure");
      AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
      http.addFilter(new JwtAuthenticationFilter(authenticationManager));
      http.addFilter(new JwtAuthorizationFilter(authenticationManager));
    }
  }

  @Bean // 리턴되는게 container에 재 등록
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // security 필터를 커스텀해서 사용
    // 다 닫고 필요한 것만 열어주기

    // iframe으로 페이지 여는거 거부
    log.debug("디버그 : SecurityConfig의 filterChain");
    http.headers().frameOptions().disable();

    // 브라우저에서 로그인폼을 요청해서 응답할 때 특정 값(random hidden key)을 들고오도록 세팅
    // 서버에서 응답해준 페이지로 들어왔는지 확인(페이지에서 요청했는지, 페이지 없이 바로 요청했는지)
    // postman으로 test 하기 위해 해제해줌
    http.csrf().disable();
    http.cors().configurationSource(configurationSource());

    // ExcpetionTranslationFilter (인증 권한 확인 필터)
    http.exceptionHandling().authenticationEntryPoint(
        (request, response, authException) -> {
          CustomResponseUtil.forbidden(response, "권한없음");
        });

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.formLogin().disable();
    http.httpBasic().disable();
    http.apply(new MyCustomDsl());

    // 로그인이 되지 않으면 들어올 수 없음
    http.authorizeHttpRequests()
        .antMatchers("/api/transaction/**").authenticated()
        .antMatchers("/api/user/**").authenticated()
        .antMatchers("/api/account/**").authenticated()
        .antMatchers("/api/admin/**").hasRole("ROLE_" + UserEnum.ADMIN) // 관리자페이지 - 인증 + 권한
        .anyRequest().permitAll(); // 이 주소들 제외하고 다 허용

    return http.build();
  }

  public CorsConfigurationSource configurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();// js의 http 요청 막음
    configuration.addAllowedHeader("*"); // 헤더
    configuration.addAllowedMethod("*"); // 허용할 메서드
    // configuration.setAllowedMethods(List.of("GET", "POST", "PUT"));

    // https://cotak.tistory.com/248
    configuration.addAllowedOriginPattern("*"); // 프론트 서버의 주소
    // configuration.addAllowedOrigin("*"); // 프론트 서버의 주소
    configuration.setAllowCredentials(true); // 클라이언트에서 쿠키, 인증헤더(인증 관련 정보) 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // 주소
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  // @CrossOrigin 전역설정
}
