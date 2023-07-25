package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.dto.requestDto.PostCreateRequestDto;
import Alumni.backend.module.service.LikeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/post/like/{post_id}")
  public ResponseEntity<? extends BasicResponse> postLike(@CurrentUser Member member, @PathVariable("post_id") Long postId) {

    String message = likeService.postLike(member, postId);

    return ResponseEntity.ok().body(new SingleResponse(message));
  }

  @PostMapping("/comment/like/{comment_id}")
  public ResponseEntity<? extends BasicResponse> commentLike(@CurrentUser Member member, @PathVariable("comment_id") Long commentId) {

    String message = likeService.commentLike(member, commentId);

    return ResponseEntity.ok().body(new SingleResponse(message));
  }

  @PostMapping("/comment/recomment/like/{comment_id}")
  public ResponseEntity<? extends BasicResponse> recommentLike(@CurrentUser Member member, @PathVariable("comment_id") Long commentId) {

    String message = likeService.recommentLike(member, commentId);

    return ResponseEntity.ok().body(new SingleResponse(message));
  }

}