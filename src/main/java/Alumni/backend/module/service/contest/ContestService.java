package Alumni.backend.module.service.contest;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.contest.ContestDetailResponseDto;
import Alumni.backend.module.dto.contest.ContestSearchResponseDto;
import Alumni.backend.module.dto.contest.TeamListDto;
import Alumni.backend.module.repository.contest.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final TeamRepository teamRepository;
    private final ContestLikeRepository contestLikeRepository;
    private final TeammateRepository teammateRepository;

    public List<ContestSearchResponseDto> contestSearch(Member member, String content) {
        List<ContestSearchResponseDto> contestResponseDtos = new ArrayList<>();

        List<Contest> contests = contestRepository.searchContest(content);
        HashMap<Long, Long> contestMap = contestLikeRepository.countContestLikesByContestId();
        HashMap<Long, Long> teamMap = teamRepository.countTeamsByContestId();

        for (Contest contest : contests) {

            Long likes = contestMap.get(contest.getId());
            Long teams = teamMap.get(contest.getId());

            ContestSearchResponseDto contestSearchResponseDto = ContestSearchResponseDto.contestSearchResponseDto(contest, likes, teams);

            if(contestLikeRepository.findByMemberIdAndContestId(member.getId(), contest.getId()).isPresent()){
                contestSearchResponseDto.setIsLikeTrue();
            }

            contestResponseDtos.add(contestSearchResponseDto);
        }

        return contestResponseDtos;
    }

    /*private Long getLikesByContestId(List<Tuple> likeTuple, Long contestId) {

        //원하는 contestId에 해당하는 결과를 찾아 카운트 값을 반환
        Long count = likeTuple.stream()
                .filter(tuple -> tuple.get(contestLike.contest.id).equals(contestId))
                .findFirst()
                .map(tuple -> tuple.get(1, Long.class)) // 두 번째 열의 값을 Long으로 변환
                .orElse(0L); // 결과가 없을 경우 0을 반환

        return count;
    }

    private Long getTeamsByContestId(List<Tuple> likeTuple, Long contestId) {

        //원하는 contestId에 해당하는 결과를 찾아 카운트 값을 반환
        Long count = likeTuple.stream()
                .filter(tuple -> tuple.get(team.contest.id).equals(contestId))
                .findFirst()
                .map(tuple -> tuple.get(1, Long.class)) // 두 번째 열의 값을 Long으로 변환
                .orElse(0L); // 결과가 없을 경우 0을 반환

        return count;
    }*/

    public ContestDetailResponseDto viewContestDetail(Member member, Long contestId) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NoExistsException("존재하지 않는 공모전"));
        Long likes = contestLikeRepository.countByContestId(contest.getId());

        ContestDetailResponseDto contestDetailResponseDto = ContestDetailResponseDto.contestDetailResponseDto(contest, likes);

        List<Team> teams = teamRepository.findByContestIdFetchJoinMemberAndImage(contest.getId());
        HashMap<Long, Long> teammateMap = teammateRepository.groupByTeamIdAndApproveIsTrue();

        List<TeamListDto> teamListDtos = teams.stream()
                .map(team -> TeamListDto.teamListDto(team, Math.toIntExact(teammateMap.get(team.getId())))).collect(Collectors.toList());
        contestDetailResponseDto.setTeamList(teamListDtos);

        //좋아요 여부
        if(contestLikeRepository.findByMemberIdAndContestId(member.getId(), contestId).isPresent())
            contestDetailResponseDto.setIsLikeTrue();

        return contestDetailResponseDto;
    }
}