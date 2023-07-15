package Alumni.backend.module.dto.requestDto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class PostModifyRequestDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    private List<String> hashTag;
}
