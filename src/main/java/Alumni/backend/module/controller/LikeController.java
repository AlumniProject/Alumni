package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.ApiDocumentResponse;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.ErrorResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.service.LikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiDocumentResponse
@Tag(name = "Like", description = "좋아요 관련 api")
@RestController
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "게시글 좋아요 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
          @ApiResponse(responseCode = "200", description = "게시글 좋아요 취소 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
  })
  @Operation(summary = "게시글 좋아요", description = "게시글 좋아요/취소 메서드 입니다.")
  @PostMapping("/post/like/{post_id}")
  public ResponseEntity<? extends BasicResponse> postLike(@CurrentUser Member member, @PathVariable("post_id") Long postId) {

    String message = likeService.postLike(member, postId);

    return ResponseEntity.ok().body(new SingleResponse(message));
  }

  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "댓글 좋아요 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
          @ApiResponse(responseCode = "200", description = "댓글 좋아요 취소 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
  })
  @Operation(summary = "댓글 좋아요", description = "댓글 좋아요/취소 메서드 입니다.")
  @PostMapping("/comment/like/{comment_id}")
  public ResponseEntity<? extends BasicResponse> commentLike(@CurrentUser Member member, @PathVariable("comment_id") Long commentId) {

    String message = likeService.commentLike(member, commentId);

    return ResponseEntity.ok().body(new SingleResponse(message));
  }

  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "대댓글 좋아요 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
          @ApiResponse(responseCode = "200", description = "대댓글 좋아요 취소 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
  })
  @Operation(summary = "대댓글 좋아요", description = "대댓글 좋아요/취소 메서드 입니다.")
  @PostMapping("/comment/recomment/like/{comment_id}")
  public ResponseEntity<? extends BasicResponse> recommentLike(@CurrentUser Member member, @PathVariable("comment_id") Long commentId) {

    String message = likeService.recommentLike(member, commentId);

    return ResponseEntity.ok().body(new SingleResponse(message));
  }

}
