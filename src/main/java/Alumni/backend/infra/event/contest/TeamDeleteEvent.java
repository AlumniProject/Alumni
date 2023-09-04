package Alumni.backend.infra.event.contest;

import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TeamDeleteEvent {

    private final List<Member> members;
    private final Team team;
}
