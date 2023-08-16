package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Team;

import java.util.Optional;

public interface TeamRepositoryCustom {

    Team findByIdFetchJoinMemberAndImage(Long teamId);

    Optional<Team> findByIdFetchJoinMember(Long teamId);

    Optional<Team> findByIdFetchJoinContest(Long teamId);
}
