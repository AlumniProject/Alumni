package Alumni.backend.module.dto.community;

import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.dto.registration.MemberResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "대댓글 DTO")
public class RecommentDto {

    @Schema(description = "대댓글 ID", example = "1", type = "Long")
    private Long id;

    @Schema(description = "좋아요 수", example = "5", type = "int")
    private Long likes;

    @Schema(description = "내용", example = "content", type = "String")
    private String content;

    @Schema(description = "작성 시간", example = "2023-07-06 07:45:31.997349", type = "String")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createtime;

    @Schema(description = "작성자 정보")
    private MemberResponseDto writer;

    public static RecommentDto getRecommentDto(Comment comment, Long likes) {
        return RecommentDto.builder()
                .id(comment.getId())
                .likes(likes == null ? 0 : likes)
                .content(comment.getContent())
                .createtime(comment.getUpdateTime())
                .writer(MemberResponseDto.getMemberResponseDto(comment.getMember()))
                .build();
    }

    public static RecommentDto getTeamRecommentDto(Comment comment) {
        return RecommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(MemberResponseDto.getMemberResponseDto(comment.getMember()))
                .build();
    }
}
