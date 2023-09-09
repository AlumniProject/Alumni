package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Contest;

import java.util.List;

public interface ContestRepositoryCustom {
    List<Contest> searchContest(String content);

    List<Contest> searchRecentContest();
}
