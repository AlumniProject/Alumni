package Alumni.backend.infra.jwt;

import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.VerifiedEmail;
import Alumni.backend.module.dto.requestDto.LoginRequestDto;
import Alumni.backend.infra.exception.EmailCodeException;
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
import javax.servlet.http.HttpServletResponse;
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
    public String verifyNewMemberOrNot(LoginRequestDto loginRequestDto) {
        // email 존재 확인
        if (!verifiedEmailRepository.existsByEmail(loginRequestDto.getEmail())) {
            return "Bad Request";
        }
        // 인증번호 확인
        VerifiedEmail verifiedEmail = verifiedEmailRepository.findByEmail(loginRequestDto.getEmail()).get();
        if (!loginRequestDto.getCertification().equals(verifiedEmail.getEmailCode())) {
            return "인증번호가 올바르지 않습니다";
        }
        verifiedEmail.verifiedTrue(); // 이메일 검증 완료
        // 신규회원인지 확인
        if (!memberRepository.existsMemberByEmail(loginRequestDto.getEmail())) {
            return "이메일 인증 완료";
        }
        // fcm token 저장
        setFcmToken(loginRequestDto.getEmail(), loginRequestDto.getFcmToken());
        return "existing";
    }

    public String verifyToken(HttpServletRequest request, HttpServletResponse response) {
        checkAccessAndRefreshHeaderValid(request);
        String accessToken = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");
        String refreshToken = request.getHeader(JwtProperties.HEADER_REFRESH)
                .replace(JwtProperties.TOKEN_PREFIX, "");
        String result = "";

        // refresh 토큰 검증
        try {
            checkTokenValid(refreshToken);
        } catch (TokenExpiredException e) { // 재로그인 필요
            return "-1";
        }
        // refresh 토큰이 유효함
        // refresh 토큰을 가진 회원을 조회
        Member memberByRefreshToken = getMemberByRefreshToken(refreshToken);
        Long memberId = memberByRefreshToken.getId();
        String email = memberByRefreshToken.getEmail();

        // refresh 토큰이 7일 이내 만료인 경우 재발급
        if (isNeedToUpdateRefreshToken(refreshToken)) {
            refreshToken = createRefreshToken(email); // 재발급한 토큰으로 교체
            setRefreshToken(email, refreshToken);
            // 헤더에 재발급한 refresh 토큰 내려주기
            response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + refreshToken);
            result += "refreshToken 갱신 완료 ";
        }

        // access 토큰 검증
        try {
            checkTokenValid(accessToken);
        } catch (TokenExpiredException e) {
            accessToken = createAccessToken(memberId, email); // access 토큰 재발급
            // 헤더에 재발급한 access 토큰 내려주기
            response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
            result += "accessToken 갱신 완료";
        }
        return result;
    }

    public void createAllTokenAddHeader(Member member, HttpServletResponse response) {
        String accessToken = createAccessToken(member.getId(), member.getEmail());
        String refreshToken = createRefreshToken(member.getEmail());

        // refresh token 저장
        setRefreshToken(member.getEmail(), refreshToken);

        // header를 통해 token 내려주기
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + refreshToken);
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
//        return memberRepository.findByEmail(email);
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Bad Request"));
    }

    @Transactional(readOnly = true)
    public Member getMemberByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }

    public void setFcmToken(String email, String fcmToken) {
//        Member member = memberRepository.findByEmail(email);
//        if (member != null) {
//            member.setFcmToken(fcmToken);
//        }

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Bad Request"));

        member.setFcmToken(fcmToken);
    }

    public void setRefreshToken(String email, String refreshToken) {
//        Member member = memberRepository.findByEmail(email);
//        if (member != null) {
//            member.setRefreshToken(refreshToken);
//        }
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Bad Request"));

        member.setRefreshToken(refreshToken);
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
            checkAccessAndRefreshHeaderValid(request);
            String refreshToken = request.getHeader(JwtProperties.HEADER_REFRESH).replace(JwtProperties.TOKEN_PREFIX, "");
            removeRefreshToken(refreshToken);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String createAccessToken(Long id, String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", id)
                .withClaim("email", email)
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

    public void checkAccessAndRefreshHeaderValid(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
        String refreshToken = request.getHeader(JwtProperties.HEADER_REFRESH);
        if (accessToken == null || !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new IllegalArgumentException("JWT_ACCESS_NOT_VALID");
        } else if (refreshToken == null || !refreshToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new IllegalArgumentException("JWT_REFRESH_NOT_VALID");
        }
    }

    public void checkAccessHeaderValid(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
        if (accessToken == null || !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new IllegalArgumentException("JWT_ACCESS_NOT_VALID");
        }
    }
}
