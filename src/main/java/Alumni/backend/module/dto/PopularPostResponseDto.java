package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularPostResponseDto {
  private Long boardId;
  private Long postId;
  private String title;
  private String content;
  private LocalDateTime createTime;
  private List<String> hashTag;
  private MemberResponseDto writer;

  public static PopularPostResponseDto getPopularPostResponseDto(Post post){
    return PopularPostResponseDto.builder()
        .boardId(post.getBoard().getId())
        .postId(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .createTime(post.getCreateTime())
        .writer(MemberResponseDto.getMemberResponseDto(post.getMember()))
        .build();
  }

  public void setHashTag(List<String> hashTag) {
    this.hashTag = hashTag;
  }

}
