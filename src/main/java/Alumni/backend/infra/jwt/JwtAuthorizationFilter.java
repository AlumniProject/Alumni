package Alumni.backend.infra.jwt;

import Alumni.backend.infra.principal.PrincipalDetails;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  MemberRepository memberRepository, JwtService jwtService) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    // 요청마다 jwt 토큰 검증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        try {
            jwtService.checkHeaderValid(request);
            String accessToken = request.getHeader(JwtProperties.HEADER_STRING)
                    .replace(JwtProperties.TOKEN_PREFIX, "");
            String refreshToken = request.getHeader(JwtProperties.HEADER_REFRESH)
                    .replace(JwtProperties.TOKEN_PREFIX, "");
            jwtService.checkTokenValid(refreshToken); // refresh 토큰 검증

            // refresh 토큰을 가진 회원을 조회
            Member memberByRefreshToken = jwtService.getMemberByRefreshToken(refreshToken);
            Long memberId = memberByRefreshToken.getId();
            String email = memberByRefreshToken.getEmail();
            String nickname = memberByRefreshToken.getNickname();

            // refresh 토큰이 7일 이내 만료인 경우 재발급
            if (jwtService.isNeedToUpdateRefreshToken(refreshToken)) {
                refreshToken = jwtService.createRefreshToken(email); // 재발급한 토큰으로 교체
                jwtService.setRefreshToken(email, refreshToken);
                // 헤더에 재발급한 refresh 토큰 내려주기
                response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + refreshToken);
            }

            // access 토큰 검증
            try {
                jwtService.checkTokenValid(accessToken);
            } catch (TokenExpiredException e) {
                accessToken = jwtService.createAccessToken(memberId, nickname, email); // access 토큰 재발급
                // 헤더에 재발급한 access 토큰 내려주기
                response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
            }

            // 권한 처리를 위해 아래와 같이 토큰을 만들어서 Authentication 객체를 만들고 세션에 저장
            PrincipalDetails principalDetails = new PrincipalDetails(memberByRefreshToken);
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
