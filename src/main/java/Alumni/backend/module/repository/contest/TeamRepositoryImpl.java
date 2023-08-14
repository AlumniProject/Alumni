package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static Alumni.backend.module.domain.QImage.image;
import static Alumni.backend.module.domain.contest.QTeam.team;
import static Alumni.backend.module.domain.registration.QMember.member;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Team findByIdFetchJoinMemberAndImage(Long teamId) {
        return jpaQueryFactory
                .selectFrom(team)
                .leftJoin(team.member, member).fetchJoin()
                .leftJoin(member.profileImage, image).fetchJoin()
                .where(team.id.eq(teamId))
                .fetchOne();
    }
}
