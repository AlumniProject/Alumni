package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Contest;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.contest.QContest.contest;
import static Alumni.backend.module.domain.contest.QContestLike.contestLike;
import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
public class ContestRepositoryImpl implements ContestRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Contest> searchContest(String content) {
        return jpaQueryFactory
                .selectFrom(contest)
                .where(getTitleContentContains(content))
                .fetch();
    }

    @Override
    public List<Contest> searchRecentContest() {
        return jpaQueryFactory
                .selectFrom(contest)
                .limit(5)
                .fetch();
    }

    @Override
    public long findLikesByContestId(Long contestId) {
        return jpaQueryFactory
                .selectFrom(contestLike)
                .where(contestLike.contest.id.eq(contestId))
                .fetchCount();
    }

    private BooleanExpression getTitleContentContains(String titleOrContent) {
        if (titleOrContent != null) {
            return getTitleContains(titleOrContent).or(getContentContains(titleOrContent));
        } else {
            return null;
        }
    }

    private BooleanExpression getTitleContains(String title) {
        return title != null ? contest.title.contains(title) : null;
    }

    private BooleanExpression getContentContains(String content) {
        return content != null ? contest.content.contains(content) : null;
    }
}
