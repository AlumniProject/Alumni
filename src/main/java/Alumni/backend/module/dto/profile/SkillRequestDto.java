package Alumni.backend.module.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SkillRequestDto {
    @Schema(name = "data", description = "스킬", example = "[\"Frontend\", \"Backend\"]")
    List<String> data;
}
