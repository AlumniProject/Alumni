package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.ApiDocumentResponse;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.ErrorResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@ApiDocumentResponse
@Tag(name = "Comment", description = "댓글 관련 api")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 작성 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 게시글입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Operation(summary = "댓글 작성", description = "댓글 작성 메서드 입니다.")
    @PostMapping("/{post_id}")
    public ResponseEntity<? extends BasicResponse> commentCreate(@CurrentUser Member member, @PathVariable("post_id") Long postId,
                                                                 @RequestBody @Valid Map<String, String> request) {
        commentService.createComment(member, postId, request.get("content"));

        return ResponseEntity.ok().body(new SingleResponse("댓글 작성 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 댓글입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "댓글 수정", description = "댓글 수정 메서드 입니다.")
    @PutMapping("/{comment_id}")
    public ResponseEntity<? extends BasicResponse> commentModify(@CurrentUser Member member, @PathVariable("comment_id") Long commentId,
                                                                 @RequestBody @Valid Map<String, String> request) {
        commentService.modifyComment(member, commentId, request.get("content"));

        return ResponseEntity.ok().body(new SingleResponse("댓글 수정 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 댓글입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 메서드 입니다.")
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<? extends BasicResponse> commentDelete(@CurrentUser Member member, @PathVariable("comment_id") Long commentId) {
        commentService.deleteComment(member, commentId);

        return ResponseEntity.ok().body(new SingleResponse("댓글 삭제 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 작성 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "상위 댓글이 존재하지 않습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 게시글입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "대댓글 작성", description = "대댓글 작성 메서드 입니다.")
    @PostMapping("/recomment/{comment_id}")
    public  ResponseEntity<? extends BasicResponse> recommentCreate(@CurrentUser Member member, @PathVariable("comment_id") Long commentId, @RequestBody @Valid Map<String, String> request){
        commentService.createRecomment(member, commentId, request.get("content"));

        return ResponseEntity.ok().body(new SingleResponse("대댓글 작성 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 수정 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 대댓글입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 게시글입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "대댓글 수정", description = "대댓글 수정 메서드 입니다.")
    @PutMapping("/recomment/{comment_id}")
    public ResponseEntity<? extends BasicResponse> recommentModify(@CurrentUser Member member, @PathVariable("comment_id") Long commentId,
                                                                 @RequestBody @Valid Map<String, String> request) {
        commentService.modifyRecomment(member, commentId, request.get("content"));

        return ResponseEntity.ok().body(new SingleResponse("대댓글 수정 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 삭제 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 대댓글입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "대댓글 삭제", description = "대댓글 삭제 메서드 입니다.")
    @DeleteMapping("/recomment/{comment_id}")
    public ResponseEntity<? extends BasicResponse> recommentDelete(@CurrentUser Member member, @PathVariable("comment_id") Long commentId) {
        commentService.deleteRecomment(member, commentId);

        return ResponseEntity.ok().body(new SingleResponse("대댓글 삭제 완료"));
    }
}
