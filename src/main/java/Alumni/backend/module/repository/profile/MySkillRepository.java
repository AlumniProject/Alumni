package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.MySkill;
import Alumni.backend.module.domain.registration.Interested;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MySkillRepository extends JpaRepository<MySkill, Long> {

    List<MySkill> findByMemberId(Long memberId);
}
