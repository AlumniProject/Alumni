package Alumni.backend.module.repository.contest;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

import static Alumni.backend.module.domain.contest.QContestLike.contestLike;
import static Alumni.backend.module.repository.contest.TeamRepositoryImpl.getLongLongHashMap;

@RequiredArgsConstructor
public class ContestLikeRepositoryImpl implements ContestLikeRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public HashMap<Long, Long> countContestLikesByContestId() {
        List<Tuple> tuples = jpaQueryFactory
                .select(contestLike.contest.id, contestLike.count())
                .from(contestLike)
                .groupBy(contestLike.contest.id)
                .fetch();

        return getLongLongHashMap(tuples);
    }
}
