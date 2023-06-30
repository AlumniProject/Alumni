package Alumni.backend.module.dto.requestDto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequestDto {
    @NotEmpty
    private String email; // 로그인 이메일
    @NotEmpty
    private String certification; // 인증번호
    @NotEmpty
    private String fcmToken; // fcm토큰
}
