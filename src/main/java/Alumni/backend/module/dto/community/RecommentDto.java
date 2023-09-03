package Alumni.backend.module.dto.community;

import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.dto.registration.MemberResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "대댓글 DTO")
public class RecommentDto {

    @Schema(description = "대댓글 ID", example = "1", type = "Long")
    private Long id;

    @Schema(description = "좋아요 수", example = "5", type = "int")
    private Integer likes;

    @Schema(description = "내용", example = "content", type = "String")
    private String content;

    @Schema(description = "작성자 정보")
    private MemberResponseDto writer;

    public static RecommentDto getRecommentDto(Comment comment, Integer likeNum) {
        return RecommentDto.builder()
                .id(comment.getId())
                .likes(likeNum)
                .content(comment.getContent())
                .writer(MemberResponseDto.getMemberResponseDto(comment.getMember()))
                .build();
    }

    public static RecommentDto getTeamRecommentDto(Comment comment) {
        return RecommentDto.builder()
                .id(comment.getId())
                .likes(null)
                .content(comment.getContent())
                .writer(MemberResponseDto.getMemberResponseDto(comment.getMember()))
                .build();
    }
}
