package Alumni.backend.module.service.profile;

import Alumni.backend.module.domain.Profile.Skill;
import Alumni.backend.module.domain.registration.InterestField;
import Alumni.backend.module.dto.community.TagDto;
import Alumni.backend.module.dto.profile.SkillDto;
import Alumni.backend.module.repository.profile.SkillRepository;
import Alumni.backend.module.repository.registration.InterestFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;

    @PostConstruct
    public void initInterestFieldData() throws IOException {
        if (skillRepository.count() == 0) { // 저장된 관심분야가 없으면 실행
            List<String> list = Arrays.asList("JAVA", "MySQL", "Spring Boot", "AWS", "JPA",
                    "Spring Security", "Python", "Flutter", "QueryDSL");
            List<Skill>skills = list.stream()
                    .map(s -> Skill.builder()
                            .skillName(s)
                            .build())
                    .collect(Collectors.toList());
            skillRepository.saveAll(skills);
        }
    }

    public List<SkillDto> findAllSkills() {
        return skillRepository.findAll().stream().map(SkillDto::new).collect(Collectors.toList());
    }
}