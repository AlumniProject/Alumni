package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.GeneralResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.contest.TeamApproveDto;
import Alumni.backend.module.dto.contest.TeamLeaderCancelDto;
import Alumni.backend.module.dto.contest.TeamRequestDto;
import Alumni.backend.module.dto.contest.TeamResponseDto;
import Alumni.backend.module.service.contest.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

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
}
