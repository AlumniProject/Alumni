package Alumni.backend.module.dto.requestDto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PostCreateRequestDto {

    @NotNull(message = "boardId 필요")
    private Long boardId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    private List<String> hashTag;
}
