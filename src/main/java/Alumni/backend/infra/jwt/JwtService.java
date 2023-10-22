package Alumni.backend.infra.jwt;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.infra.exception.UnAuthorizedException;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.VerifiedEmail;
import Alumni.backend.module.dto.registration.LoginRequestDto;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.VerifiedEmailRepository;
import Alumni.backend.module.service.RedisService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Getter
@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final RedisService redisService;
    @Value("${jwt.secret-key}")
    private String SECRET;
    @Value("${jwt.access-token.expiration-time}")
    private int EXPIRATION_TIME;
    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_EXPIRATION_TIME;

    /**
     * 토큰값이 유효한지 확인 -> 신규회원인지 아닌지 확인
     */
    public String verifyNewMemberOrNot(LoginRequestDto loginRequestDto) {
        // email 존재 확인
        Optional<VerifiedEmail> byEmail = verifiedEmailRepository.findByEmail(loginRequestDto.getEmail());
        if (byEmail.isEmpty()) {
            return "존재하지 않는 회원";
        }
        // 인증번호 확인
        VerifiedEmail verifiedEmail = byEmail.get();
        if (!loginRequestDto.getCertification().equals(verifiedEmail.getEmailCode())) {
            return "인증번호가 올바르지 않습니다";
        }
        verifiedEmail.verifiedTrue(); // 이메일 검증 완료
        // 신규회원인지 확인
        if (!memberRepository.existsMemberByEmail(loginRequestDto.getEmail())) {
            return "이메일 인증 완료";
        }
        // fem token 존재하는지 확인
        String fcmToken = loginRequestDto.getFcmToken();
        if (fcmToken == null || fcmToken.isBlank()) {
            return "blank";
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
        String email = JWT.require(Algorithm.HMAC512(SECRET)).build()
                .verify(refreshToken).getClaim("email").asString();
        Member memberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));
        Long memberId = memberByEmail.getId();

        // refresh 토큰이 7일 이내 만료인 경우 재발급
        if (isNeedToUpdateRefreshToken(refreshToken)) {
            refreshToken = createRefreshToken(email); // 재발급한 토큰으로 교체
            updateRefreshToken(refreshToken);
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
        saveRefreshToken(refreshToken);

        // header를 통해 token 내려주기
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + refreshToken);
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Bad Request"));
    }

    public void setFcmToken(String email, String fcmToken) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Bad Request"));

        member.setFcmToken(fcmToken);
    }

    public void saveAccessToken(String accessToken) {
        Date expireTime = getExpireTime(accessToken);
        redisService.setValueWithDate(accessToken, "logout", expireTime);
    }

    public void saveRefreshToken(String refreshToken) {
        Date expireTime = getExpireTime(refreshToken);
        redisService.setValueWithDate(refreshToken, "refresh", expireTime);
    }

    public void updateRefreshToken(String refreshToken) {
        Date expireTime = getExpireTime(refreshToken);
        redisService.updateValueWithDate(refreshToken, expireTime);
    }

    public void removeRefreshToken(String refreshToken) {
        redisService.deleteValue(refreshToken);
    }

    public void logout(Member member, HttpServletRequest request) {
        try { // refresh 토큰 삭제
            checkAccessAndRefreshHeaderValid(request);
            String refreshToken = request.getHeader(JwtProperties.HEADER_REFRESH).replace(JwtProperties.TOKEN_PREFIX, "");
            removeRefreshToken(refreshToken);
            // fcm 토큰 삭제
            Member currentUser = memberRepository.findById(member.getId())
                    .orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));
            currentUser.setFcmToken("");
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad Request");
        }

        // blackList에 access 토큰 추가
        String accessToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
        //blackListService.saveBlackList(accessToken);
        saveAccessToken(accessToken);
    }

    /*public void isExistBlackListByAccessToken(String accessToken) {
        if (blackListRepository.existsBlackListByAccessToken(accessToken)) {
            throw new NoExistsException("NoExistsException");
        }
    }*/

    public void isExistBlackListByAccessToken(String accessToken) {
        String value = redisService.getValue(accessToken);
        if (value != null) {
            throw new NoExistsException("NoExistsException");
        }
    }

    public String createAccessToken(Long id, String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("id", id)
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(SECRET));
    }

    public String createRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(SECRET));
    }

    public void checkTokenValid(String token) {
        JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);
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

    public Date getExpireTime(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token).getExpiresAt();
    }

    public boolean isNeedToUpdateRefreshToken(String refreshToken) {
        try {
            Date expiresAt = getExpireTime(refreshToken);
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
            throw new UnAuthorizedException("JWT_ACCESS_NOT_VALID");
        } else if (refreshToken == null || !refreshToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new UnAuthorizedException("JWT_ACCESS_NOT_VALID");
        }
    }

    public void checkAccessHeaderValid(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
        if (accessToken == null || !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new IllegalArgumentException("JWT_ACCESS_NOT_VALID");
        }
    }

    public boolean verifyToken(String token){
        throw new IllegalArgumentException("토큰 검증");
    }

}
