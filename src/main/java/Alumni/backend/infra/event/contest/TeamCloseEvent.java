package Alumni.backend.infra.event.contest;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TeamCloseEvent {

    private final List<Member> members;
}
