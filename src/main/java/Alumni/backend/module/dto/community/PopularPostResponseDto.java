package Alumni.backend.module.dto.community;

import Alumni.backend.module.domain.community.Post;
import java.time.LocalDateTime;
import java.util.List;

import Alumni.backend.module.dto.registration.MemberResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "인기 게시글 response")
public class PopularPostResponseDto {
  @Schema(description = "게시판 ID", example = "1", type = "Long")
  @NotNull
  private Long boardId;

  @Schema(description = "게시글 ID", example = "3", type = "Long")
  @NotNull
  private Long postId;

  @Schema(description = "게시글 제목", example = "스프링 부트", type = "String")
  @NotEmpty
  private String title;

  @Schema(description = "게시글 내용", example = "스프링 부트는 이런거에요", type = "String")
  @NotEmpty
  private String content;

  @Schema(description = "게시글 생성시간", example = "2023-07-06 07:45:31.997349", type = "LocalDateTime")
  @NotEmpty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createTime;

  @Schema(description = "해시태그 리스트", example = "[\"python\", \"Java\"]", type = "List<String>")
  private List<String> hashTag;

  @Schema(description = "작성자 정보", type = "Object")
  @NotEmpty
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
