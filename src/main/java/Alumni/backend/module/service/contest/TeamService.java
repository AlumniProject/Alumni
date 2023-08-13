package Alumni.backend.module.service.contest;

import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.contest.TeamCreateDto;
import Alumni.backend.module.repository.contest.TeamRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public void createTeam(Member member, Long contestId, TeamCreateDto teamCreateDto) {

    }
}
