package Alumni.backend.module.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostResponseDto {

    private Long boardId;
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private List<String> hashTag;
    private MemberResponseDto writer;
}
