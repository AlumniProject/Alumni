package Alumni.backend.module.repository.contest;

import java.util.HashMap;

public interface ContestLikeRepositoryCustom {
    HashMap<Long, Long> countContestLikesByContestId();
}
