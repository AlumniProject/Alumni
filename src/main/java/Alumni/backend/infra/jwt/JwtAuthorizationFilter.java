package Alumni.backend.infra.jwt;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.infra.principal.PrincipalDetails;
import Alumni.backend.module.domain.registration.Member;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;
    private final String SECRET;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtService jwtService, String SECRET) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.SECRET = SECRET;
    }

    // 요청마다 jwt 토큰 검증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        try {
            jwtService.checkAccessHeaderValid(request);
            String accessToken = request.getHeader(JwtProperties.HEADER_STRING)
                    .replace(JwtProperties.TOKEN_PREFIX, "");

            jwtService.isExistBlackListByAccessToken(accessToken);

            try {
                jwtService.checkTokenValid(accessToken);
            } catch (TokenExpiredException e) {
                request.setAttribute(JwtProperties.EXCEPTION, "ACCESS_TOKEN_EXPIRED");
            }

            String email = JWT.require(Algorithm.HMAC512(SECRET)).build()
                    .verify(accessToken).getClaim("email").asString();
            Member memberByEmail = jwtService.getMemberByEmail(email);

            PrincipalDetails principalDetails = new PrincipalDetails(memberByEmail);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails,
                    null,
                    principalDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (TokenExpiredException e) {
            request.setAttribute(JwtProperties.EXCEPTION, "JWT_NOT_VALID");
        } catch (NoExistsException e) {
            request.setAttribute(JwtProperties.EXCEPTION, "다시 로그인해주세요");
        } catch (IllegalArgumentException e) {
            request.setAttribute(JwtProperties.EXCEPTION, "JWT_ACCESS_NOT_VALID");
        } catch (Exception e) {
            request.setAttribute(JwtProperties.EXCEPTION, "RUNTIME_ERROR");
        }
        /**
         * 필터에서 발생하는 에러는 ControllerAdvice 처리불가
         * 따라서 각각 상황에 맞는 예외를 구분해서 적고 인증 예외를 처리하는 AuthenticationEntryPoint 클래스에서 처리
         * 권한 관련 검증은 AccessDeniedHandler 처리
         * */
        chain.doFilter(request, response);
    }
}
