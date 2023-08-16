package Alumni.backend.module.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TeamRequestDto {

    @NotNull
    @Schema(description = "공모전 id", required = true, example = "1")
    private Long contestId;
    @NotEmpty
    @Schema(description = "팀원 모집글 제목", required = true, example = "팀원 모집합니다")
    private String title;
    @NotEmpty
    @Schema(description = "활동 지역", required = true, example = "인천 미추홀구")
    private String region;
    @NotNull
    @Schema(description = "총 인원", required = true, example = "5")
    private Integer total;
    @NotEmpty
    @Schema(description = "팀원 모집글 내용", required = true, example = "같이 공모전 나가실 분 모집합니다")
    private String content;
}
