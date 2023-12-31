package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Team;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static Alumni.backend.module.domain.QImage.image;
import static Alumni.backend.module.domain.contest.QContest.contest;
import static Alumni.backend.module.domain.contest.QTeam.team;
import static Alumni.backend.module.domain.registration.QMember.member;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Team findByIdFetchJoinMemberAndImage(Long teamId) {
        return jpaQueryFactory
                .selectFrom(team)
                .join(team.member, member).fetchJoin()
                .leftJoin(member.profileImage, image).fetchJoin()
                .where(team.id.eq(teamId))
                .fetchOne();
    }

    @Override
    public Optional<Team> findByIdFetchJoinMember(Long teamId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(team)
                .join(team.member, member).fetchJoin()
                .where(team.id.eq(teamId))
                .fetchOne());
    }

    @Override
    public Optional<Team> findByIdFetchJoinContest(Long teamId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(team)
                .join(team.contest, contest).fetchJoin()
                .where(team.id.eq(teamId))
                .fetchOne());
    }

    @Override
    public List<Team> findByContestIdFetchJoinMemberAndImage(Long contestId) {
        return jpaQueryFactory
                .selectFrom(team)
                .join(team.member, member).fetchJoin()
                .leftJoin(member.profileImage, image).fetchJoin()
                .where(team.contest.id.eq(contestId))
                .fetch();
    }

    @Override
    public long findTeamsByContestId(Long contestId) {
        return jpaQueryFactory
                .selectFrom(team)
                .where(team.contest.id.eq(contestId))
                .fetchCount();
    }


    @Override
    public HashMap<Long, Long> countTeamsByContestId() {
        List<Tuple> tuples = jpaQueryFactory
                .select(team.contest.id, team.count())
                .from(team)
                .groupBy(team.contest.id)
                .fetch();

        return getLongLongHashMap(tuples);
    }

    public static HashMap<Long, Long> getLongLongHashMap(List<Tuple> tuples) {
        HashMap<Long, Long> hashMap = new HashMap<>();
        tuples.forEach(tuple -> hashMap.put(tuple.get(0, Long.class), tuple.get(1, Long.class)));
        return hashMap;
    }
}
