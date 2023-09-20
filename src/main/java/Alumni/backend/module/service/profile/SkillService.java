package Alumni.backend.module.service.profile;

import Alumni.backend.module.domain.Profile.Skill;
import Alumni.backend.module.repository.profile.SkillRepository;
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
            List<String> list = Arrays.asList("HTML", "CSS", "JavaScript", "React.js", "Angular", "Vue.js", "Node.js", "Python", "Ruby", "Java", "PHP",
                    ".NET", "Django", "Flask", "Express.js", "Spring Boot", "MySQL", "PostgreSQL", "MongoDB", "Oracle", "SQL Server", "AWS", "Azure",
                    "GCP", "Docker", "Kubernetes", "Heroku", "Flask", "Git", "GitHub", "GitLab", "Bitbucket", "JavaScript", "Python", "C#", "Swift",
                    "Kotlin", "Bootstrap", "Material_UI", "Jest", "Selenium", "JUnit", "pytest", "OAuth", "JWT", "Visual Studio Code",
                    "Intellij IDEA", "Eclipse", "JSON", "Restful API", "EC2", "RDS", "S3", "JPA", "Spring Data JPA", "Spring", "Spring Security", "Query DSL");
            List<Skill>skills = list.stream()
                    .map(s -> Skill.builder()
                            .skillName(s)
                            .build())
                    .collect(Collectors.toList());
            skillRepository.saveAll(skills);
        }
    }
}