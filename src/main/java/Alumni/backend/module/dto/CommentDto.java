package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "댓글 DTO")
public class CommentDto {

    @Schema(description = "댓글 ID", example = "1", type = "Long")
    private Long id;
    @Schema(description = "좋아요 수", example = "5", type = "int")
    private int likes;
    @Schema(description = "내용", example = "content", type = "String")
    private String content;
    @Schema(description = "작성자 정보")
    private MemberResponseDto writer;
    @Schema(description = "대댓글 리스트")
    private List<RecommentDto> recommentList;

    public static CommentDto getCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .likes(comment.getLikeNum())
                .content(comment.getContent())
                .writer(MemberResponseDto.getMemberResponseDto(comment.getMember()))
                .build();
    }

    public void setRecommentList(List<RecommentDto> recommentList) {
        this.recommentList = recommentList;
    }
}
