package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Teammate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static Alumni.backend.module.domain.QImage.image;
import static Alumni.backend.module.domain.contest.QTeam.team;
import static Alumni.backend.module.domain.contest.QTeammate.teammate;
import static Alumni.backend.module.domain.registration.QMember.member;
import static Alumni.backend.module.domain.registration.QUniversity.university;

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

    @Override
    public List<Teammate> findByTeamIdFetchJoinMemberAndImageAndUniv(Long teamId) {
        return jpaQueryFactory
                .selectFrom(teammate)
                .join(teammate.member, member).fetchJoin()
                .join(member.university, university).fetchJoin()
                .leftJoin(member.profileImage, image).fetchJoin()
                .where(teammate.team.id.eq(teamId))
                .fetch();
    }

    @Override
    public List<Teammate> findByTeamIdFetchJoinMember(Long teamId) {
        return jpaQueryFactory
                .selectFrom(teammate)
                .join(teammate.member, member).fetchJoin()
                .where(teammate.team.id.eq(teamId))
                .fetch();
    }

    @Override
    public List<Teammate> findByTeamIdFetchJoinMemberWithApprove(Long teamId) {
        return jpaQueryFactory
                .selectFrom(teammate)
                .join(teammate.member, member).fetchJoin()
                .where(teammate.team.id.eq(teamId),
                        teammate.approve.eq(true))
                .fetch();
    }
}
