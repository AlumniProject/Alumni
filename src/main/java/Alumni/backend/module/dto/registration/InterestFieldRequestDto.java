package Alumni.backend.module.dto.registration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class InterestFieldRequestDto {
    @Schema(name = "data", description = "관심분야", example = "[\"Frontend\", \"Backend\"]")
    List<String> data;
}
