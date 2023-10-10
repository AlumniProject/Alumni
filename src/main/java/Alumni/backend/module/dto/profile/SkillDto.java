package Alumni.backend.module.dto.profile;

import Alumni.backend.module.domain.Profile.Skill;
import lombok.Data;

@Data
public class SkillDto {
    private Long id;
    private String name;

    public SkillDto(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getSkillName();
    }
}
