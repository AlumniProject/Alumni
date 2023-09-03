package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.dto.community.CommentDto;
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
    private Boolean closed; // 마감 여부
    private MemberResponseDto writer;
    private List<CommentDto> commentList;

    public static TeamResponseDto getTeamResponseDto(Team team, Integer current) {
        return TeamResponseDto.builder()
                .teamId(team.getId())
                .title(team.getTitle())
                .content(team.getContent())
                .total(team.getHeadcount())
                .current(current)
                .region(team.getRegion())
                .closed(team.getClosed())
                .writer(MemberResponseDto.getMemberResponseDto(team.getMember()))
                .build();
    }

    public void setCommentList(List<CommentDto> commentList) {
        this.commentList = commentList;
    }
}
