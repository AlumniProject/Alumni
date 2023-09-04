package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Contest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestDetailResponseDto {

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
    private String link;
    @NotEmpty
    private String poster;
    @NotEmpty
    private Integer likes;
    @NotEmpty
    private Boolean isLike;
    private List<TeamListDto> teamList;

    public static ContestDetailResponseDto contestDetailResponseDto(Contest contest) {
        return ContestDetailResponseDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .content(contest.getContent())
                .period(contest.getPeriod())
                .field(contest.getField())
                .link(contest.getLink())
                .poster(contest.getPoster())
                .likes(contest.getLikeNum())
                .build();
    }

    public void setTeamList(List<TeamListDto> teamList){
        this.teamList = teamList;
    }

    public void setIsLikeTrue(){
        this.isLike = true;
    }
}
