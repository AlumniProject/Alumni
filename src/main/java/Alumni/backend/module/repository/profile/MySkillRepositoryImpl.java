package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.MySkill;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.Profile.QMySkill.mySkill;
import static Alumni.backend.module.domain.Profile.QSkill.skill;
import static Alumni.backend.module.domain.registration.QMember.member;
import static Alumni.backend.module.domain.registration.QUniversity.university;

@RequiredArgsConstructor
public class MySkillRepositoryImpl implements MySkillRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Long> recommendMemberByBackend(List<String> skills) {
        return jpaQueryFactory
                .select(mySkill.member.id)
                .from(mySkill)
                .join(mySkill.skill, skill)
                .where(skill.skillName.in(skills))
                .groupBy(mySkill.member)
                .orderBy(mySkill.member.count().desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<MySkill> findByMemberIdInFetchJoinSkill(List<Long> memberIds) {
        return jpaQueryFactory
                .selectFrom(mySkill)
                .leftJoin(mySkill.member, member).fetchJoin()
                .leftJoin(member.university, university).fetchJoin()
                .leftJoin(mySkill.skill, skill).fetchJoin()
                .where(mySkill.member.id.in(memberIds))
                .fetch();
    }
}
