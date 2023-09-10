package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Team;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Optional;

public interface TeamRepositoryCustom {

    Team findByIdFetchJoinMemberAndImage(Long teamId);

    Optional<Team> findByIdFetchJoinMember(Long teamId);

    Optional<Team> findByIdFetchJoinContest(Long teamId);

    List<Team> findByContestIdFetchJoinMemberAndImage(Long contestId);

    long findTeamsByContestId(Long contestId);

    List<Tuple> countTeamsByContestId();
}
