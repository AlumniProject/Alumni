package Alumni.backend.infra.event;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecommentCreateEvent {

    private final Member member;
}
