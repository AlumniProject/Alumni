package Alumni.backend.infra.jwt;

import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.VerifiedEmail;
import Alumni.backend.module.dto.LoginRequestDto;
import Alumni.backend.module.exception.EmailCodeException;
import Alumni.backend.module.repository.MemberRepository;
import Alumni.backend.module.repository.VerifiedEmailRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Service
@Getter
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;
    private final VerifiedEmailRepository verifiedEmailRepository;

    /**
     * 토큰값이 유효한지 확인 -> 신규회원인지 아닌지 확인
     */
    public String verify(LoginRequestDto loginRequestDto) {
        // email 존재 확인
        if (!verifiedEmailRepository.existsByEmail(loginRequestDto.getEmail())) {
            throw new IllegalArgumentException("Bad Request");
        }
        // 인증번호 확인
        VerifiedEmail verifiedEmail = verifiedEmailRepository.findByEmail(
                loginRequestDto.getEmail()).get();
        if (!loginRequestDto.getCertification().equals(verifiedEmail.getEmailCode())) {
            throw new EmailCodeException("인증번호가 올바르지 않습니다");
        }
        verifiedEmail.verifiedTrue(); // 이메일 검증 완료
        // 신규회원인지 확인
        if (!memberRepository.existsMemberByEmail(loginRequestDto.getEmail())) {
            return "new";
        }
        return "existing";
    }

    @Transactional(readOnly = true)
    public Member getMemberByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }

    public void setRefreshToken(String email, String refreshToken) {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            member.setRefreshToken(refreshToken);
        }
    }

    public void removeRefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken);
        if (member != null) {
            member.setRefreshToken(null);
        }
    }

    @Transactional(readOnly = true)
    public void logout(HttpServletRequest request) {
        try {
            checkHeaderValid(request);
            String refreshToken = request.getHeader(JwtProperties.HEADER_REFRESH).replace(JwtProperties.TOKEN_PREFIX, "");
            removeRefreshToken(refreshToken);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String createAccessToken(Long id, String nickname, String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", id)
                .withClaim("userNickname", nickname)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public String createRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public void checkTokenValid(String token) {
        JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
    }

    // true : 만료
    // false : 유효
    public boolean isExpiredToken(String token) {
        try {
            checkTokenValid(token);
        } catch (TokenExpiredException e) {
            return true;
        }
        return false;
    }

    public boolean isNeedToUpdateRefreshToken(String refreshToken) {
        try {
            Date expiresAt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                    .build().verify(refreshToken).getExpiresAt();
            Date current = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, 7);

            Date after7dayFromCurrent = calendar.getTime();

            if (expiresAt.before(after7dayFromCurrent)) {
                return true;
            }
        } catch (TokenExpiredException e) {
            return true;
        }
        return false;
    }

    public void checkHeaderValid(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
        String refreshToken = request.getHeader(JwtProperties.HEADER_REFRESH);
        if (accessToken == null || !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new IllegalArgumentException("JWT_ACCESS_NOT_VALID");
        } else if (refreshToken == null || !refreshToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new IllegalArgumentException("JWT_REFRESH_NOT_VALID");
        }
    }
}
