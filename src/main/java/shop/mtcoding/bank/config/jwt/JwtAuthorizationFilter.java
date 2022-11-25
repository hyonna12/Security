package shop.mtcoding.bank.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.util.CustomResponseUtil;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AuthenticationManager authenticationManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 1. 헤더 검증 / 헤더 검증 실패하면 바로 return - 더이상 실행되지 않도록
        if (!isheaderVerify(request, response)) {
            return;
        }

        // 2. 토큰 파싱하기 (Bearer 없애기) - Bearer 은 토큰 헤더의 프로토콜
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");
        log.debug("디버그2 : token" + token);

        // 토큰 검증해서 userDetails 만듦
        try {
            // 3. 토큰 검증
            LoginUser loginUser = JwtProcess.verify(token);

            // 4. 임시 세션 생성(한번 req-resp 할때까지만 필요한 세션) - 토큰 집어 넣음
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser,
                    null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5. 다음 필터로 이동
            chain.doFilter(request, response);
            return;
        } catch (Exception e) {
            CustomResponseUtil.fail(response, e.getMessage());
        }
    }

    // 헤더 검증
    private Boolean isheaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            CustomResponseUtil.fail(null, "토큰 헤더가 없습니다.");
            return false;
        } else {
            return true;
        }
    }

}