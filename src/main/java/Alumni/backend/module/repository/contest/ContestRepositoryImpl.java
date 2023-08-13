package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Contest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static Alumni.backend.module.domain.contest.QContest.contest;

@RequiredArgsConstructor
public class ContestRepositoryImpl implements ContestRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<List<Contest>> findByContent(String content) {
        List<Contest> contests = jpaQueryFactory
                .selectFrom(contest)
                .where(contest.content.contains(content))
                .fetch();
        return Optional.ofNullable(contests);
    }
}
