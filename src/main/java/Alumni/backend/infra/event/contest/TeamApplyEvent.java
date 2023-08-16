package Alumni.backend.infra.event.contest;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeamApplyEvent {

    private final Member member;
}
