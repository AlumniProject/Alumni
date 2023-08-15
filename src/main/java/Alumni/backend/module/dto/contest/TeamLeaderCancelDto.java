package Alumni.backend.module.dto.contest;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TeamLeaderCancelDto {

    @NotNull
    private Long memberId;
}
