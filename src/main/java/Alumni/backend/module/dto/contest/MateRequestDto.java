package Alumni.backend.module.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MateRequestDto {

    @Schema(description = "추천 분야", example = "Back-end")
    @NotEmpty(message = "내용은 필수입니다.")
    private String field;
}
