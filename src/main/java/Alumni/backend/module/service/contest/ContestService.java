package Alumni.backend.module.service.contest;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.dto.contest.ContestDetailResponseDto;
import Alumni.backend.module.dto.contest.ContestSearchResponseDto;
import Alumni.backend.module.dto.contest.TeamListDto;
import Alumni.backend.module.dto.registration.TermsDto;
import Alumni.backend.module.repository.contest.ContestRepository;
import Alumni.backend.module.repository.contest.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final TeamRepository teamRepository;

    public List<ContestSearchResponseDto> contestSearch(String content) {
        List<ContestSearchResponseDto> contestResponseDtos = new ArrayList<>();

        List<Contest> contests = contestRepository.findByContent(content).
                orElseThrow(() -> new NoExistsException("검색 결과가 없음"));

        for (Contest contest : contests) {
            contestResponseDtos.add(ContestSearchResponseDto.contestSearchResponseDto(contest));
        }

        return contestResponseDtos;
    }

    public ContestDetailResponseDto viewContestDetail(Long contestId) {

        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NoExistsException("존재하지 않는 공모전"));
        ContestDetailResponseDto contestDetailResponseDto = ContestDetailResponseDto.contestDetailResponseDto(contest);

        if(teamRepository.findByContestId(contest.getId()).isPresent()){
            List<Team> teams = teamRepository.findByContestId(contest.getId()).get();
            List<TeamListDto> teamListDtos = new ArrayList<>();

            for (Team team : teams) {
                teamListDtos.add(TeamListDto.teamListDto(team));
            }
            contestDetailResponseDto.setTeamList(teamListDtos);
        }

        return contestDetailResponseDto;
    }
}