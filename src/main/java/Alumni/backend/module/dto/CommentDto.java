package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentDto {

    private Long id;
    private int likes;
    private String content;
    private MemberResponseDto writer;
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
