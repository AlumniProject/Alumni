package Alumni.backend.module.repository.contest;

import com.querydsl.core.Tuple;

import java.util.List;

public interface ContestLikeRepositoryCustom {
    List<Tuple> countContestLikesByContestId();
}
