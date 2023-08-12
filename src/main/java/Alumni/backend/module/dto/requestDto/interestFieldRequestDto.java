package Alumni.backend.module.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class interestFieldRequestDto {
    @Schema(name = "data", description = "관심분야", example = "[\"Frontend\", \"Backend\"]")
    List<String> data;
}
