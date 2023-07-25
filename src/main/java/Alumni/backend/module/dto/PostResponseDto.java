package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private Long boardId;
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private List<String> hashTag;
    private int likes;
    private int comments;
    private MemberResponseDto writer;
    private List<CommentDto> commentList;

    public static PostResponseDto getPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .boardId(post.getBoard().getId())
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createTime(post.getCreateTime())
                .likes(post.getLikeNum())
                .comments(post.getCommentNum())
                .writer(MemberResponseDto.getMemberResponseDto(post.getMember()))
                .build();
    }

    public void setHashTag(List<String> hashTag) {
        this.hashTag = hashTag;
    }

    public void setCommentList(List<CommentDto> commentList) {
        this.commentList = commentList;
    }
}
