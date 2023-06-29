package Alumni.backend.infra.jwt;

import Alumni.backend.infra.principal.PrincipalDetails;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.repository.MemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
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

            try {
                jwtService.checkTokenValid(accessToken);
            } catch (TokenExpiredException e) {
                request.setAttribute(JwtProperties.EXCEPTION, "ACCESS_TOKEN_EXPIRED");
            }

            String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
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
        } catch (Exception e) {
            request.setAttribute(JwtProperties.EXCEPTION, "JWT_UNEXPECTED_ERROR");
        }
        /**
         * 필터에서 발생하는 에러는 ControllerAdvice 처리불가
         * 따라서 각각 상황에 맞는 예외를 구분해서 적고 인증 예외를 처리하는 AuthenticationEntryPoint 클래스에서 처리
         * 권한 관련 검증은 AccessDeniedHandler 처리
         * */
        chain.doFilter(request, response);
    }
}
