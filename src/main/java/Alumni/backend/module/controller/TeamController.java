package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.CommentRequestDto;
import Alumni.backend.module.dto.contest.TeamApproveDto;
import Alumni.backend.module.dto.contest.TeamLeaderCancelDto;
import Alumni.backend.module.dto.contest.TeamRequestDto;
import Alumni.backend.module.dto.contest.TeamResponseDto;
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

    @PostMapping("/contestDummy")
    public ResponseEntity<? extends BasicResponse> contestDummy() {
        teamService.saveDummyContest();
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @PostMapping("/{contestId}")
    public ResponseEntity<? extends BasicResponse> createTeam(@CurrentUser Member member,
                                                              @PathVariable("contestId") Long contestId,
                                                              @RequestBody @Valid TeamRequestDto teamRequestDto) {
        teamService.createTeam(member, contestId, teamRequestDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<? extends BasicResponse> modifyTeam(@CurrentUser Member member,
                                                              @PathVariable("teamId") Long teamId,
                                                              @RequestBody @Valid TeamRequestDto teamRequestDto) {
        teamService.modifyTeam(member, teamId, teamRequestDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<? extends BasicResponse> deleteTeam(@CurrentUser Member member,
                                                              @PathVariable("teamId") Long teamId) {
        teamService.teamDelete(member, teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<? extends BasicResponse> teamDetail(@PathVariable("teamId") Long teamId) {
        TeamResponseDto teamResponseDto = teamService.teamDetail(teamId);
        return ResponseEntity.ok().body(new GeneralResponse<>(teamResponseDto, "SUCCESS"));
    }

    @PostMapping("/member/{teamId}")
    public ResponseEntity<? extends BasicResponse> applyTeam(@CurrentUser Member member,
                                                             @PathVariable("teamId") Long teamId) {
        teamService.applyTeam(member, teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @PostMapping("/member/cancel/{teamId}")
    public ResponseEntity<? extends BasicResponse> cancelTeam(@CurrentUser Member member,
                                                              @PathVariable("teamId") Long teamId) {
        teamService.cancelTeam(member, teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @PostMapping("/approve/{teamId}")
    public ResponseEntity<? extends BasicResponse> approveTeam(@CurrentUser Member member,
                                                               @RequestBody @Valid TeamApproveDto teamApproveDto,
                                                               @PathVariable("teamId") Long teamId) {
        teamService.approveTeam(member, teamId, teamApproveDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @PostMapping("/cancel/{teamId}")
    public ResponseEntity<? extends BasicResponse> teamLeaderCancelMate(@CurrentUser Member member,
                                                                        @RequestBody @Valid TeamLeaderCancelDto teamLeaderCancelDto,
                                                                        @PathVariable("teamId") Long teamId) {
        teamService.teamLeaderCancelMate(member, teamId, teamLeaderCancelDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @PostMapping("/closed/{teamId}")
    public ResponseEntity<? extends BasicResponse> closedTeam(@CurrentUser Member member,
                                                              @PathVariable("teamId") Long teamId) {
        teamService.closedTeam(member, teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @GetMapping("/member/all/{teamId}")
    public ResponseEntity<? extends BasicResponse> allTeamList(@PathVariable("teamId") Long teamId) {
        return ResponseEntity.ok().body(teamService.allTeamList(teamId));
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
