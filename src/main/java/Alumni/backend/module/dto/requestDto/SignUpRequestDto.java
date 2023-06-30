package Alumni.backend.module.dto.requestDto;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SignUpRequestDto {
    @NotEmpty
    private String email;
    @AssertTrue @NotNull
    private boolean agreement1;
    @AssertTrue @NotNull
    private boolean agreement2;

    @NotEmpty
    private String classOf;
    @NotEmpty
    private String major;
    @NotEmpty
    private String nickname;
    @NotEmpty
    private String fcmToken;
}
