package Alumni.backend.module.dto.contest;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TeamRequestDto {

    @NotNull
    private Long contestId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String region;
    @NotNull
    private Integer total;
    @NotEmpty
    private String content;
}
