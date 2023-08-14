package Alumni.backend.module.dto.community;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TeamApproveDto {

    @NotNull
    private List<String> memberList;
}
