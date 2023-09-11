package Alumni.backend.module.domain.Profile;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MySkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_skill_id", nullable = false)
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
