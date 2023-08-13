package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.dto.registration.MemberResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamResponseDto {

    private Long teamId;
    private String title;
    private String content;
    private Integer total; // 총 인원수
    private Integer current; // 현재 인원
    private String region; // 활동 지역
    private MemberResponseDto writer;
    private List<TeamCommentDto> commentList;

    public static TeamResponseDto getTeamResponseDto(Team team, Integer currentCount) {
        return TeamResponseDto.builder()
                .teamId(team.getId())
                .title(team.getTitle())
                .content(team.getContent())
                .total(team.getHeadcount())
                .current(currentCount)
                .region(team.getRegion())
                .writer(MemberResponseDto.getMemberResponseDto(team.getMember()))
                .build();
    }

    public void setCommentList(List<TeamCommentDto> commentList) {
        this.commentList = commentList;
    }
}
