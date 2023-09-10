package Alumni.backend.infra.response;

import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.dto.contest.TeamApplyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "팀원 신청 회원리스트 조회 성공 형식 Response")
public class TeamListResponse extends BasicResponse {

    @Schema(description = "응답 코드", defaultValue = "200")
    private int code;
    @Schema(description = "응답 메시지", example = "SUCCESS")
    private String message;
    @Schema(description = "팀장 ID", example = "1")
    private Long teamLeaderId;
    @Schema(description = "모집 마감 여부", example = "false")
    private Boolean isClosed;
    @Schema(description = "총 인원수", example = "5")
    private int total;
    @Schema(description = "현재 팀원수", example = "3")
    private int current;
    @Schema(description = "데이터의 개수", example = "5")
    private int count;
    @Schema(description = "데이터 결과")
    private List<TeamApplyDto> data;

    public TeamListResponse(List<TeamApplyDto> teamApplyDtos, Team team, Integer current, String message) {
        this.code = 200;
        this.message = message;
        this.teamLeaderId = team.getMember().getId();
        this.isClosed = team.getClosed();
        this.total = team.getHeadcount();
        this.current = current == null ? 0 : current;
        this.count = teamApplyDtos.size();
        this.data = teamApplyDtos;
    }
}
