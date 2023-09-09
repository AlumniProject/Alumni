package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Contest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestSearchResponseDto {
    @NotNull
    private Long contestId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    private String period;
    @NotEmpty
    private String field;
    @NotEmpty
    private String poster;
    @NotEmpty
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
