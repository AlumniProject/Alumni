package Alumni.backend.module.domain.Profile;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class MySkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_skill_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MySkill createMySkill(Member member, Skill skill) {
        MySkill mySkill = new MySkill();

        mySkill.member = member;
        mySkill.skill = skill;

        return mySkill;
    }

    public void addMemberSkills(Member member) {
        member.getMySkills().add(this);
    }
}
