package Alumni.backend.module.service.contest;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.dto.contest.ContestDetailResponseDto;
import Alumni.backend.module.dto.contest.ContestSearchResponseDto;
import Alumni.backend.module.dto.contest.TeamListDto;
import Alumni.backend.module.repository.contest.ContestRepository;
import Alumni.backend.module.repository.contest.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final TeamRepository teamRepository;

    public List<ContestSearchResponseDto> contestSearch(String content) {
        List<ContestSearchResponseDto> contestResponseDtos = new ArrayList<>();

        List<Contest> contests = contestRepository.searchContest(content);

        for (Contest contest : contests) {
            contestResponseDtos.add(ContestSearchResponseDto.contestSearchResponseDto(contest));
        }

        return contestResponseDtos;
    }

    public ContestDetailResponseDto viewContestDetail(Long contestId) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NoExistsException("존재하지 않는 공모전"));
        ContestDetailResponseDto contestDetailResponseDto = ContestDetailResponseDto.contestDetailResponseDto(contest);

        List<Team> teams = teamRepository.findByContestIdFetchJoinMemberAndImage(contest.getId());
        if (!teams.isEmpty()) {
            List<TeamListDto> teamListDtos = teams.stream().map(TeamListDto::teamListDto).collect(Collectors.toList());
            contestDetailResponseDto.setTeamList(teamListDtos);
        }

        return contestDetailResponseDto;
    }
}