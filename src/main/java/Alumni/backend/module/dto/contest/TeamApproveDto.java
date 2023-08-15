package Alumni.backend.module.dto.contest;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class TeamApproveDto {

    @NotEmpty
    private List<Long> memberList;
}
