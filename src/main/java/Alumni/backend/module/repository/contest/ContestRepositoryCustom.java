package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Contest;
import com.querydsl.core.Tuple;

import java.util.List;

public interface ContestRepositoryCustom {
    List<Contest> searchContest(String content);

    List<Contest> searchRecentContest();

    long findLikesByContestId(Long contestId);
}
