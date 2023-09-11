package Alumni.backend.module.domain.Profile;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id", nullable = false)
    private Long id;

    private String skillName;
}
