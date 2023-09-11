package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.MySkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySkillRepository extends JpaRepository<MySkill, Long> {
}
