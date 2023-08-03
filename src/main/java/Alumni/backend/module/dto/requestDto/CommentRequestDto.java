package Alumni.backend.module.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentRequestDto {

    @Schema(description = "댓글 내용", example = "댓글입니당~~")
    private String content;
}
