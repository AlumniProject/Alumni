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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final MemberRepository memberRepository;

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
      MemberRepository memberRepository) {
    super(authenticationManager);
    this.memberRepository = memberRepository;
  }

  // 요청마다 jwt 토큰 검증
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {
    String header = request.getHeader(JwtProperties.HEADER_STRING); // Authorization 헤더 value 찾기
    if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
      // 토큰값이 없거나 유효하지 않으면 security 설정 필요없이 chain에 그대로 전달
      chain.doFilter(request, response);
      return;
    }
    // Bearer prefix 삭제
    String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

    // token에서 id로 member찾고 securitycontext 설정
    Long memberId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
        .getClaim("id").asLong();
    if (memberId != null) {
      Optional<Member> optionalMember = memberRepository.findById(memberId);

      // 권한 처리를 위해 아래와 같이 토큰을 만들어서 Authentication 객체를 만들고 세션에 저장
      PrincipalDetails principalDetails = new PrincipalDetails(optionalMember.get());
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          principalDetails,
          null,
          principalDetails.getAuthorities()
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    chain.doFilter(request, response);
  }
}
