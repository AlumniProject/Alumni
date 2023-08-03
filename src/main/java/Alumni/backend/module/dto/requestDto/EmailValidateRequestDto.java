package Alumni.backend.module.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailValidateRequestDto {
    @Schema(name = "email", description = "이메일", example = "abcd1234@yu.ac.kr")
    String email;
}
