package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Contest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestSearchResponseDto {
    private Long contestId;
    private String title;
    private String content;
    @NotEmpty
    private String period;
    @NotEmpty
    private String field;
    @NotEmpty
    private String period;
    private String field;
    private String poster;
    private Integer likes;
    @NotEmpty
    private Boolean isLike;
    @NotNull
    private Integer teams;

    public static ContestSearchResponseDto contestSearchResponseDto(Contest contest) {
        return ContestSearchResponseDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .content(contest.getContent())
                .period(contest.getPeriod())
                .field(contest.getField())
                .poster(contest.getPoster())
                .likes(contest.getLikeNum())
                .isLike(false)
                .teams(contest.getTeamNum())
                .build();
    }

    public void setIsLikeTrue(){
        this.isLike = true;
    }
}
