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
    private Long contestId;
    private String title;
    private String content;
    @NotEmpty
    private String period;
    @NotEmpty
    private String field;
    private String poster;
    private Long likes;
    @NotEmpty
    private Boolean isLike;
    @NotNull
    private Long teams;

    public static ContestSearchResponseDto contestSearchResponseDto(Contest contest, Long likes, Long teams) {
        return ContestSearchResponseDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .content(contest.getContent())
                .period(contest.getPeriod())
                .field(contest.getField())
                .poster(contest.getPoster())
                .likes(likes == null ? 0 : likes)
                .isLike(false)
                .teams(teams == null ? 0 : teams)
                .build();
    }

    public void setIsLikeTrue(){
        this.isLike = true;
    }
}
