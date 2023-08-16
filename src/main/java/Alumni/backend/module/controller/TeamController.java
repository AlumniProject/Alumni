package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.CommentRequestDto;
import Alumni.backend.module.dto.contest.TeamCreateDto;
import Alumni.backend.module.service.community.CommentService;
import Alumni.backend.module.service.contest.TeamService;
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
@ApiDocumentAuthResponse
@ApiDocumentGlobalResponse
@Tag(name = "Team", description = "팀 관련 api")
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final CommentService commentService;

    @PostMapping("/{contestId}")
    public ResponseEntity<? extends BasicResponse> createTeam(@CurrentUser Member member,
                                                              @PathVariable("contestId") Long contestId,
                                                              @RequestBody @Valid TeamCreateDto teamCreateDto) {
        teamService.createTeam(member, contestId, teamCreateDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 모집글 댓글 작성", description = "팀 모집글에 댓글을 작성하는 메서드 입니다..")
    @Parameter(name = "team_id", description = "댓글을 작성할 팀 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/comment/{team_id}")
    public ResponseEntity<? extends BasicResponse> createTeamComment(@CurrentUser Member member, @PathVariable("team_id") Long teamId,
                                                                     @RequestBody @Valid CommentRequestDto commentRequestDto){
        commentService.createTeamComment(member, teamId, commentRequestDto.getContent());

        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 모집글 댓글 삭제", description = "팀 모집글에 작성한 댓글을 삭제하는 메서드 입니다.")
    @Parameter(name = "comment_id", description = "삭제할 댓글 id", required = true, example = "1", in = ParameterIn.PATH)
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<? extends BasicResponse> deleteTeamComment(@CurrentUser Member member, @PathVariable("comment_id") Long commentId){
        commentService.deleteTeamComment(member, commentId);

        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    /**
     * 대댓글
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "상위 댓글이 존재하지 않습니다" + "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 모집글 대댓글 작성", description = "팀 모집글에 대댓글을 작성하는 메서드 입니다..")
    @Parameter(name = "comment_id", description = "대댓글을 작성할 상위 댓글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/recomment/{comment_id}")
    public ResponseEntity<? extends BasicResponse> createTeamRecomment(@CurrentUser Member member, @PathVariable("comment_id") Long commentId,
                                                                     @RequestBody @Valid CommentRequestDto commentRequestDto){
        commentService.createTeamRecomment(member, commentId, commentRequestDto.getContent());

        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "상위 댓글이 존재하지 않습니다" + "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 모집글 대댓글 삭제", description = "팀 모집글에 작성한 대댓글을 삭제하는 메서드 입니다.")
    @Parameter(name = "comment_id", description = "삭제할 대댓글 id", required = true, example = "1", in = ParameterIn.PATH)
    @DeleteMapping("/recomment/{comment_id}")
    public ResponseEntity<? extends BasicResponse> deleteTeamRecomment(@CurrentUser Member member, @PathVariable("comment_id") Long commentId){
        commentService.deleteTeamRecomment(member, commentId);

        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }
}
