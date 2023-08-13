package Alumni.backend.module.service.contest;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.dto.contest.ContestResponseDto;
import Alumni.backend.module.repository.contest.ContestRepository;
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

    public List<ContestResponseDto> contestSearch(String content) {
        List<ContestResponseDto> contestResponseDtos = new ArrayList<>();

        List<Contest> contests = contestRepository.findByContent(content).
                orElseThrow(() -> new NoExistsException("검색 결과가 없음"));

        for (Contest contest : contests) {
            contestResponseDtos.add(ContestResponseDto.getContestResponseDto(contest));
        }

        return contestResponseDtos;
    }
}