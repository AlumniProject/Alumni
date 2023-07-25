package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommentDto {

    private Long id;
    private int likes;
    private String content;
    private MemberResponseDto writer;

    public static RecommentDto getRecommentDto(Comment comment) {
        return RecommentDto.builder()
                .id(comment.getId())
                .likes(comment.getLikeNum())
                .content(comment.getContent())
                .writer(MemberResponseDto.getMemberResponseDto(comment.getMember()))
                .build();
    }
}
