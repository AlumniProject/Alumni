package Alumni.backend.module.dto.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class PostModifyRequestDto {
    @NotEmpty
    @Schema(description = "제목", required = true, example = "안녕하세요요세하녕안")
    private String title;

    @NotEmpty
    @Schema(description = "내용", required = true, example = "게시글 수정된 내용입니다")
    private String content;

    @Schema(description = "기술 게시판인 경우 해시태그", example = "[\"React\", \"Unity\"]")
    private List<String> hashTag;
}