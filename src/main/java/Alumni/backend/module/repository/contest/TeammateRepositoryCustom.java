package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Teammate;

import java.util.List;
import java.util.Optional;

public interface TeammateRepositoryCustom {

    Optional<Teammate> findByMemberIdAndTeamIdFetchJoinTeam(Long memberId, Long teamId);

    List<Teammate> findByTeamIdFetchJoinMemberAndImageAndUniv(Long teamId);

    List<Teammate> findByTeamIdFetchJoinMember(Long teamId);

    List<Teammate> findByTeamIdFetchJoinMemberWithApprove(Long teamId);
}
