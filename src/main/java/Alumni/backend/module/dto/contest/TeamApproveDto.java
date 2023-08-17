package Alumni.backend.module.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class TeamApproveDto {

    @NotEmpty
    @Schema(description = "팀원으로 승인할 지원자 ID 리스트", example = "[1, 2, 3]")
    private List<Long> memberList;
}
