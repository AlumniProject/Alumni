package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Team;

public interface TeamRepositoryCustom {

    Team findByIdFetchJoinMemberAndImage(Long teamId);
}
