package Alumni.backend.module.dto.profile;

import Alumni.backend.module.domain.community.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@Schema(description = "내 게시물 DTO")
public class MyPostResponseDto {
    @Schema(description = "게시판 ID", example = "1", type = "Long")
    private Long boardId;

    @Schema(description = "게시글 ID", example = "1", type = "Long")
    private Long postId;

    @Schema(description = "제목", example = "title", type = "String")
    private String title;

    @Schema(description = "내용", example = "content", type = "String")
    private String content;

    @Schema(description = "작성 시간", example = "2023-07-06 07:45:31.997349", type = "String")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createTime;

    @Schema(description = "해시태그 리스트", example = "[\"python\", \"Java\"]", type = "List<String>")
    private List<String> hashTag;

    @Schema(description = "좋아요 수", example = "101", type = "int")
    private long likes;

    @Schema(description = "댓글 수", example = "5", type = "int")
    private long comments;


    public static MyPostResponseDto getMyPosts(Post post, Long likes, Long comments){
        return MyPostResponseDto.builder()
                .boardId(post.getBoard().getId())
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createTime(post.getCreateTime())
                .likes(likes == null ? 0 : likes)
                .comments(comments == null ? 0 : comments)
                .build();
    }

    public void setHashTag(List<String> hashTag) {
        this.hashTag = hashTag == null ? Collections.emptyList() : hashTag;
    }
}
