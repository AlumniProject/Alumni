package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.CommentRequestDto;
import Alumni.backend.module.service.community.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ApiDocumentGlobalResponse
@ApiDocumentAuthResponse
@Tag(name = "Commnet", description = "댓글 관련 api")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 작성 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 게시글입니다" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "댓글 작성", description = "댓글 작성 메서드 입니다.")
    @Parameter(name = "post_id", description = "게시판 id", required = true, example = "1", in = ParameterIn.PATH)//pathvariable 설명
    @PostMapping("/{post_id}")
    public ResponseEntity<? extends BasicResponse> commentCreate(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("post_id") Long postId, @RequestBody @Valid CommentRequestDto commentRequestDto) {
        commentService.createComment(member, postId, commentRequestDto.getContent());

        return ResponseEntity.ok().body(new SingleResponse("댓글 작성 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 댓글입니다" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "댓글 수정", description = "댓글 수정 메서드 입니다.")
    @Parameter(name = "comment_id", description = "수정할 댓글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PutMapping("/{comment_id}")
    public ResponseEntity<? extends BasicResponse> commentModify(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("comment_id") Long commentId,
                                                                 @RequestBody @Valid CommentRequestDto commentRequestDto) {
        commentService.modifyComment(member, commentId, commentRequestDto.getContent());

        return ResponseEntity.ok().body(new SingleResponse("댓글 수정 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 댓글입니다" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 메서드 입니다.")
    @Parameter(name = "comment_id", description = "삭제할 댓글 id", required = true, example = "1", in = ParameterIn.PATH)
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<? extends BasicResponse> commentDelete(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("comment_id") Long commentId) {
        commentService.deleteComment(member, commentId);

        return ResponseEntity.ok().body(new SingleResponse("댓글 삭제 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 작성 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "상위 댓글이 존재하지 않습니다" + "\t\n존재하지 않는 게시글 입니다" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "대댓글 작성", description = "대댓글 작성 메서드 입니다.")
    @Parameter(name = "comment_id", description = "상위 댓글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/recomment/{comment_id}")
    public  ResponseEntity<? extends BasicResponse> recommentCreate(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("comment_id") Long commentId, @RequestBody @Valid CommentRequestDto commentRequestDto){
        commentService.createRecomment(member, commentId, commentRequestDto.getContent());

        return ResponseEntity.ok().body(new SingleResponse("대댓글 작성 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 수정 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 대댓글입니다" + "\t\n존재하지 않는 게시글입니다" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "대댓글 수정", description = "대댓글 수정 메서드 입니다.")
    @Parameter(name = "comment_id", description = "수정할 대댓글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PutMapping("/recomment/{comment_id}")
    public ResponseEntity<? extends BasicResponse> recommentModify(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("comment_id") Long commentId,
                                                                 @RequestBody @Valid CommentRequestDto commentRequestDto) {
        commentService.modifyRecomment(member, commentId, commentRequestDto.getContent());

        return ResponseEntity.ok().body(new SingleResponse("대댓글 수정 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 삭제 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 대댓글입니다" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "대댓글 삭제", description = "대댓글 삭제 메서드 입니다.")
    @Parameter(name = "comment_id", description = "삭제할 대댓글 id", required = true, example = "1", in = ParameterIn.PATH)
    @DeleteMapping("/recomment/{comment_id}")
    public ResponseEntity<? extends BasicResponse> recommentDelete(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("comment_id") Long commentId) {
        commentService.deleteRecomment(member, commentId);

        return ResponseEntity.ok().body(new SingleResponse("대댓글 삭제 완료"));
    }
}
