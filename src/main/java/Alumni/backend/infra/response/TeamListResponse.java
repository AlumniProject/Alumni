package Alumni.backend.infra.response;

import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.dto.contest.TeamApplyDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamListResponse extends BasicResponse {

    private int code;
    private String message;
    private int total;
    private int current;
    private int count;
    private List<TeamApplyDto> data;

    public TeamListResponse(List<TeamApplyDto> teamApplyDtos, Team team, String message) {
        this.code = 200;
        this.message = message;
        this.total = team.getHeadcount();
        this.current = team.getCurrent();
        this.count = teamApplyDtos.size();
        this.data = teamApplyDtos;
    }
}
