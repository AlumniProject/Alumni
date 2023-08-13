package Alumni.backend.module.service.contest;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.contest.TeamRequestDto;
import Alumni.backend.module.dto.contest.TeamResponseDto;
import Alumni.backend.module.repository.contest.ContestRepository;
import Alumni.backend.module.repository.contest.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;

    public void saveDummyContest() {
        Contest contest = Contest.builder()
                .contestUrl("www.test.com")
                .field("IT")
                .title("testTitle")
                .content("testContent")
                .likeNum(0)
                .teamNum(5)
                .build();
        contestRepository.save(contest);
    }

    public void createTeam(Member member, Long contestId, TeamRequestDto teamRequestDto) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 공모전"));
        teamRepository.save(Team.createTeam(teamRequestDto, member, contest));
    }

    public void modifyTeam(Member member, Long teamId, TeamRequestDto teamRequestDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 팀"));
        if (!team.getMember().getId().equals(member.getId())) // 수정하는 사람이 작성한 팀모집인지 확인
            throw new IllegalArgumentException("Bad Request");
        team.teamModify(teamRequestDto);
    }

    public void teamDelete(Member member, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 팀"));
        if (!team.getMember().getId().equals(member.getId())) // 수정하는 사람이 작성한 팀모집인지 확인
            throw new IllegalArgumentException("Bad Request");
        //TODO : teammate 테이블에서 관련 데이터 삭제 + 달린 댓글 삭제
        teamRepository.delete(team);
    }

    public void teamDetail(Long teamId) {

    }
}
