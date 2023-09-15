package Alumni.backend.module.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SkillRequestDto {
    @Schema(name = "skill", description = "스킬", example = "[\"Flutter\", \"JPA\"]")
    @NotNull @NotEmpty
    List<String> skill;
}
