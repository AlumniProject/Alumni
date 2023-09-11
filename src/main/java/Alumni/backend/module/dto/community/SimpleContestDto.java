package Alumni.backend.module.dto.community;

import Alumni.backend.module.domain.contest.Contest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "홈화면 공모전 DTO")
public class SimpleContestDto {
    @Schema(description = "공모전 ID", example = "1", type = "Long")
    private Long contestId;
    @Schema(description = "공모전 제목", example = "제목제목제목", type = "String")
    private String title;
    @Schema(description = "공모전 기간", example = "2023-08-29 ~ 2023-10-04", type = "String")
    private String period;
    @Schema(description = "공모전 포스터", example = "https://~image.jpg", type = "String")
    private String poster;


    public static SimpleContestDto getSimpleContestDto(Contest contest){
        return SimpleContestDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .period(contest.getPeriod())
                .poster(contest.getPoster())
                .build();
    }
}
