package Alumni.backend.infra.jwt;

import Alumni.backend.infra.principal.PrincipalDetails;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    // Authentication 객체를 만들어서 리턴
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("attempt login");

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        try {
            loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 토큰값 유효한지 + 신규회원이지 확인
        // 신규회원이면 jwt 토큰 생성하지 않음
        String verify = jwtService.verify(loginRequestDto);
        if (verify.equals("new")) {
            return null;
        }

        // UsernamePassword 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(),
                null
        );

        // authenticate 메서드를 호출하면 loadUserByUsername 호출
        return authenticationManager.authenticate(authenticationToken);
    }

    // 로그인 인증 완료되면 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Member member = principalDetails.getMember();
        String accessToken = jwtService.createAccessToken(member.getId(), member.getNickname(), member.getEmail());
        String refreshToken = jwtService.createRefreshToken(member.getEmail());

        // refresh token 저장
        jwtService.setRefreshToken(member.getEmail(), refreshToken);

        // header를 통해 token 내려주기
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + refreshToken);

        // 기존회원이 로그인 시도 하는 경우
        setBodyResponse(response, "로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        // 신규회원이 로그인 시도 하는 경우
        setBodyResponse(response, "이메일 인증 완료");
    }

    private void setBodyResponse(HttpServletResponse response, String message) throws IOException {
        response.getWriter().write(objectMapper.writeValueAsString(new SingleResponse(message)));
    }
}
