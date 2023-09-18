package Alumni.backend.module.dto.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PostCreateRequestDto {

    @NotNull(message = "boardId 필요")
    @Schema(description = "게시판 id", required = true, example = "1")
    private Long boardId;

    @NotEmpty
    @Schema(description = "제목", required = true, example = "안녕하세요")
    private String title;

    @NotEmpty
    @Schema(description = "내용", required = true, example = "게시글 내용입니다")
    private String content;

    @Schema(description = "기술 게시판인 경우 해시태그", example = "[\"python\", \"Java\"]")
    private List<String> hashTag;

    private Boolean isEnabledGPT;
}
