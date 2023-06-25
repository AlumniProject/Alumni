package Alumni.backend.module.service;

import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.VerifiedEmail;
import Alumni.backend.module.dto.LoginRequestDto;
import Alumni.backend.module.exception.EmailCodeException;
import Alumni.backend.module.repository.MemberRepository;
import Alumni.backend.module.repository.VerifiedEmailRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifiedEmailService {

  private final VerifiedEmailRepository verifiedEmailRepository;
  private final MemberRepository memberRepository;

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
}
