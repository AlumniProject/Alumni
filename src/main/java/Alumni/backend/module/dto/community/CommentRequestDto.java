package Alumni.backend.module.dto.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CommentRequestDto {

    @Schema(description = "댓글 내용", example = "댓글입니당~~")
    @NotEmpty(message = "댓글 내용은 필수입니다.")
    private String content;

    @Builder
    private CommentRequestDto(String content) {
        this.content = content;
    }
}
