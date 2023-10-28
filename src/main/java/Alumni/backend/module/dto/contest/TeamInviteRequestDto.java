package Alumni.backend.module.dto.contest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamInviteRequestDto {
    @NotNull
    private Long teamId;
    @NotNull
    private Long contestId;
    @NotEmpty
    private String title;
}
