package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.contest.TeamRequestDto;
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
        teamService.teamDetail(teamId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }
}
