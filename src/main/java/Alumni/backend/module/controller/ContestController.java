package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.contest.ContestDetailResponseDto;
import Alumni.backend.module.dto.contest.ContestSearchResponseDto;
import Alumni.backend.module.service.community.LikeService;
import Alumni.backend.module.service.contest.ContestService;
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

import java.io.IOException;
import java.util.List;

@ApiDocumentGlobalResponse
@ApiDocumentAuthResponse
@Tag(name = "Contest", description = "공모전 관련 api")
@RestController
@RequestMapping("/contest")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;
    private final LikeService likeService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "공모전 검색", description = "공모전 내용으로 검색하는 메서드 입니다.")
    @Parameter(name = "content", description = "검색 내용", example = "개발", in = ParameterIn.QUERY)
    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> searchContest(@Schema(hidden = true) @CurrentUser Member member, @RequestParam(value = "content", required = false) String content) throws IOException, InterruptedException {

        List<ContestSearchResponseDto> contestResponseDtos = contestService.contestSearch(member, content);

        return ResponseEntity.ok().body(new GeneralResponse<>(contestResponseDtos, "SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 공모전" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "공모전 상세보기", description = "공모전에 대한 상세한 내용을 볼 수 있는 메소드입니다.")
    @Parameter(name = "contest_id", description = "공모전 게시글 아이디", example = "1", in = ParameterIn.PATH)
    @GetMapping("/{contest_id}")
    public ResponseEntity<? extends BasicResponse> viewContestDetail(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("contest_id") Long contestId){
        ContestDetailResponseDto contestDetailResponseDto = contestService.viewContestDetail(member, contestId);

        return ResponseEntity.ok().body(new GeneralResponse<>(contestDetailResponseDto, "SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공모전 좋아요 완료" + "<br>공모전 좋아요 취소 완료", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 공모전" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "공모전 좋아요", description = "공모전에 좋아요/취소를 할 수 있는 메소드입니다.")
    @Parameter(name = "contest_id", description = "공모전 아이디", example = "1", in = ParameterIn.PATH)
    @PostMapping("/like/{contest_id}")
    public ResponseEntity< ? extends BasicResponse> contestLike(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("contest_id") Long contestId){
        String message = likeService.contestLike(member, contestId);

        return ResponseEntity.ok().body(new SingleResponse(message));
    }
}
