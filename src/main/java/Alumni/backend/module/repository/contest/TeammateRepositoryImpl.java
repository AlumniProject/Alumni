package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Teammate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static Alumni.backend.module.domain.contest.QTeam.team;
import static Alumni.backend.module.domain.contest.QTeammate.teammate;

@RequiredArgsConstructor
public class TeammateRepositoryImpl implements TeammateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Teammate> findByMemberIdAndTeamIdFetchJoinTeam(Long memberId, Long teamId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(teammate)
                        .join(teammate.team, team).fetchJoin()
                        .where(teammate.member.id.eq(memberId),
                                teammate.team.id.eq(teamId))
                        .fetchOne()
        );
    }
}
