package Alumni.backend.module.repository.contest;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.community.QComment.comment;
import static Alumni.backend.module.domain.contest.QContest.contest;
import static Alumni.backend.module.domain.contest.QContestLike.contestLike;
import static Alumni.backend.module.domain.registration.QMember.member;

@RequiredArgsConstructor
public class ContestLikeRepositoryImpl implements ContestLikeRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> countContestLikesByContestId() {
        return jpaQueryFactory
                .select(contestLike.contest.id, contestLike.count())
                .from(contestLike)
                .groupBy(contestLike.contest.id)
                .fetch();
    }
}
