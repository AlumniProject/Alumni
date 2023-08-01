package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.dto.PostResponseDto;
import Alumni.backend.module.dto.requestDto.PostCreateRequestDto;
import Alumni.backend.module.dto.requestDto.PostModifyRequestDto;
import Alumni.backend.module.dto.requestDto.PostSearch;
import Alumni.backend.module.service.PostService;
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
import java.util.List;

@Tag(name = "Post", description = "게시글 관련 api")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 등록 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "ACCESS_TOKEN_EXPIRED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Operation(summary = "게시글 작성", description = "게시글 작성 메서드 입니다.")
    @PostMapping("/create")
    public ResponseEntity<? extends BasicResponse> postCreate(@CurrentUser Member member,
                                                              @RequestBody @Valid PostCreateRequestDto postCreateRequestDto) {
        postService.postCreate(member, postCreateRequestDto);

        return ResponseEntity.ok().body(new SingleResponse("게시글 등록 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "ACCESS_TOKEN_EXPIRED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Operation(summary = "게시글 수정", description = "게시글 수정 메서드 입니다.")
    @PutMapping("/{id}")
    public ResponseEntity<? extends BasicResponse> postModify(@CurrentUser Member member, @PathVariable("id") Long postId,
                                                              @RequestBody @Valid PostModifyRequestDto postModifyRequestDto) {
        postService.postModify(member, postId, postModifyRequestDto);

        return ResponseEntity.ok().body(new SingleResponse("게시글 수정 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "ACCESS_TOKEN_EXPIRED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 메서드 입니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<? extends BasicResponse> postDelete(@CurrentUser Member member, @PathVariable("id") Long postId) {
        postService.postDelete(member, postId);

        return ResponseEntity.ok().body(new SingleResponse("게시글 삭제 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 게시글 조회 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "ACCESS_TOKEN_EXPIRED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Operation(summary = "게시글 전체 조회", description = "모든 게시글 조회 메서드 입니다.")
    @GetMapping("/all")
    public ResponseEntity<? extends BasicResponse> findAllPost(@CurrentUser Member member) {
        List<String> tagRankList = postService.tagRank();
        List<PostResponseDto> postResponseDtos = postService.findAllPosts(member);
        return ResponseEntity.ok()
                .body(new PostSearchResponse<>(postResponseDtos, tagRankList, "모든 게시글 조회 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 검색 결과 전송 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "ACCESS_TOKEN_EXPIRED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Operation(summary = "게시글 검색", description = "게시글 검색 메서드 입니다.")
    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> postSearch(@CurrentUser Member member,
                                                              @ModelAttribute PostSearch postSearch) {
        return ResponseEntity.ok().body(postService.search(member, postSearch));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 상세보기 전송 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "ACCESS_TOKEN_EXPIRED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Operation(summary = "게시글 상세보기", description = "게시글 상세보기 메서드 입니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<? extends BasicResponse> viewPostDetail(@PathVariable("postId") Long postId) {
        PostResponseDto postDetails = postService.getPostDetails(postId);
        return ResponseEntity.ok().body(new GeneralResponse<>(postDetails, "게시글 상세보기 전송 완료"));
    }
}
