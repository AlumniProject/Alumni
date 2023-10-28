package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.MySkill;

import java.util.List;

public interface MySkillRepositoryCustom {

    List<Long> recommendMemberByBackend(List<String> backendSkills);

    List<MySkill> findByMemberIdInFetchJoinSkill(List<Long> memberIds);
}
