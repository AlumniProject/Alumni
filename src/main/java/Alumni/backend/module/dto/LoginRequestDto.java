package Alumni.backend.module.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequestDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String token;

    private String fcmToken;
}