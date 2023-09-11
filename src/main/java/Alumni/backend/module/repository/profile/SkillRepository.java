package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
