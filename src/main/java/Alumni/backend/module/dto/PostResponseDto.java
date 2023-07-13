package Alumni.backend.module.dto;

import lombok.*;

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

    /*public static PostResponseDto createPostResponseDto(Long boardId, Long postId, String title,
                                                        String content, LocalDateTime crateTime,
                                                        List<String> hashTag, MemberResponseDto writer) {
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.boardId = boardId;
        postResponseDto.postId = postId;
        postResponseDto.title = title;
        postResponseDto.content = content;
        postResponseDto.createTime = crateTime;
        postResponseDto.hashTag = hashTag;
        postResponseDto.writer = writer;
        return postResponseDto;
    }*/

    public void setHashTag(List<String> hashTag) {
        this.hashTag = hashTag;
    }

    public void setWriter(MemberResponseDto memberResponseDto) {
        this.writer = memberResponseDto;
    }
}
