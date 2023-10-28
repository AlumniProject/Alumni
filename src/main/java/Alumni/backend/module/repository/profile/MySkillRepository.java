package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.MySkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(readOnly = true)
public interface MySkillRepository extends JpaRepository<MySkill, Long>, MySkillRepositoryCustom {

    List<MySkill> findByMemberId(Long memberId);
}
