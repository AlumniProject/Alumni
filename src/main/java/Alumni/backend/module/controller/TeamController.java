package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.CommentRequestDto;
import Alumni.backend.module.dto.contest.*;
import Alumni.backend.module.service.community.CommentService;
import Alumni.backend.module.service.contest.TeamService;
import Alumni.backend.module.service.profile.SkillService;
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
import java.util.List;

@ApiDocumentAuthResponse
@ApiDocumentGlobalResponse
@Tag(name = "Team", description = "팀 관련 api")
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final CommentService commentService;
    private final SkillService skillService;


//    @PostMapping("/contestDummy")
//    public ResponseEntity<? extends BasicResponse> contestDummy() {
//        teamService.saveDummyContest();
//        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
//    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요" + "<br>NO_EXIST_CONTEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 생성", description = "팀 모집글을 생성하는 메서드입니다.")
    @Parameter(name = "contest_id", description = "공모전 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/{contest_id}")
    public ResponseEntity<? extends BasicResponse> createTeam(@Schema(hidden = true) @CurrentUser Member member,
                                                              @PathVariable("contest_id") Long contestId,
                                                              @RequestBody @Valid TeamRequestDto teamRequestDto) {
        teamService.createTeam(member, contestId, teamRequestDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요" + "<br>NO_EXIST_CONTEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 수정", description = "팀 모집글을 수정하는 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PutMapping("/{team_id}")
    public ResponseEntity<? extends BasicResponse> modifyTeam(@Schema(hidden = true) @CurrentUser Member member,
                                                              @PathVariable("team_id") Long teamId,
                                                              @RequestBody @Valid TeamRequestDto teamRequestDto) {
        teamService.modifyTeam(member, teamId, teamRequestDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요" + "<br>NO_EXIST_TEAM",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 삭제", description = "팀 모집글을 삭제하는 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @DeleteMapping("/{team_id}")
    public ResponseEntity<? extends BasicResponse> deleteTeam(@Schema(hidden = true) @CurrentUser Member member,
                                                              @PathVariable("team_id") Long teamId) {
        teamService.teamDelete(member, teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요" + "<br>NO_EXIST_TEAM",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 상세보기", description = "팀원 모집글 상세보기 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/{team_id}")
    public ResponseEntity<? extends BasicResponse> teamDetail(@PathVariable("team_id") Long teamId) {
        TeamResponseDto teamResponseDto = teamService.teamDetail(teamId);
        return ResponseEntity.ok().body(new GeneralResponse<>(teamResponseDto, "SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요"
                    + "<br>NO_EXIST_TEAM" + "<br>CLOSED" + "<br>EXIST_TEAMMATE",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀원 신청", description = "팀원으로 신청하는 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/member/{team_id}")
    public ResponseEntity<? extends BasicResponse> applyTeam(@Schema(hidden = true) @CurrentUser Member member,
                                                             @PathVariable("team_id") Long teamId) {
        teamService.applyTeam(member, teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요" + "<br>NO_EXIST_TEAMMATE",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀원 신청 취소", description = "팀원을 취소하는 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/member/cancel/{team_id}")
    public ResponseEntity<? extends BasicResponse> cancelTeam(@Schema(hidden = true) @CurrentUser Member member,
                                                              @PathVariable("team_id") Long teamId) {
        teamService.cancelTeam(member, teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요" + "<br>NO_EXIST_TEAM",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀원 등록", description = "팀장이 지원자를 팀원으로 승인하는 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/approve/{team_id}")
    public ResponseEntity<? extends BasicResponse> approveTeam(@Schema(hidden = true) @CurrentUser Member member,
                                                               @RequestBody @Valid TeamApproveDto teamApproveDto,
                                                               @PathVariable("team_id") Long teamId) {
        teamService.approveTeam(member, teamId, teamApproveDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요"
                    + "<br>NO_EXIST_TEAM" + "<br>NO_EXIST_TEAMMATE",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀원 승인 취소", description = "팀장이 승인한 팀원을 취소하는 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/cancel/{team_id}")
    public ResponseEntity<? extends BasicResponse> teamLeaderCancelMate(@Schema(hidden = true) @CurrentUser Member member,
                                                                        @RequestBody @Valid TeamLeaderCancelDto teamLeaderCancelDto,
                                                                        @PathVariable("team_id") Long teamId) {
        teamService.teamLeaderCancelMate(member, teamId, teamLeaderCancelDto);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요" + "<br>NO_EXIST_TEAM",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀원 모집 마감", description = "팀장이 팀 모집을 마감하는 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/closed/{team_id}")
    public ResponseEntity<? extends BasicResponse> closedTeam(@Schema(hidden = true) @CurrentUser Member member,
                                                              @PathVariable("team_id") Long teamId) {
        teamService.closedTeam(member, teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TeamListResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요" + "<br>NO_EXIST_TEAM",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀원 신청 회원리스트", description = "팀 모집글에 신청한 회원리스트를 조회하는 메서드입니다.")
    @Parameter(name = "team_id", description = "팀 모집글 id", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/member/all/{team_id}")
    public ResponseEntity<? extends BasicResponse> allTeamList(@PathVariable("team_id") Long teamId) {
        return ResponseEntity.ok().body(teamService.allTeamList(teamId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀 모집글 댓글 작성", description = "팀 모집글에 댓글을 작성하는 메서드 입니다.")
    @Parameter(name = "team_id", description = "댓글을 작성할 팀 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/comment/{team_id}")
    public ResponseEntity<? extends BasicResponse> createTeamComment(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("team_id") Long teamId,
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
    public ResponseEntity<? extends BasicResponse> deleteTeamComment(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("comment_id") Long commentId){
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
    public ResponseEntity<? extends BasicResponse> createTeamRecomment(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("comment_id") Long commentId,
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
    public ResponseEntity<? extends BasicResponse> deleteTeamRecomment(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("comment_id") Long commentId){
        commentService.deleteTeamRecomment(member, commentId);

        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR <br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR <br>HTTP_REQUEST_ERROR <br>Bad Request <br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팀원 추천", description = "팀 모집에 팀원 추천을 요청하는 API")
    @PostMapping("/recommend")
    public ResponseEntity<? extends BasicResponse> recommendTeammate(@RequestBody @Valid MateRequestDto mateRequestDto) {
        List<MateResponseDto> mateResponseDtos = skillService.recommendMate(mateRequestDto);
        return ResponseEntity.ok().body(new GeneralResponse<>(mateResponseDtos, "SUCCESS"));
    }
}
