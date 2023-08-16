package Alumni.backend.module.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TeamLeaderCancelDto {

    @NotNull
    @Schema(description = "회원 id", required = true, example = "1")
    private Long memberId;
}
