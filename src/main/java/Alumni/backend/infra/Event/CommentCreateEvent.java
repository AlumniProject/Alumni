package Alumni.backend.infra.Event;

import Alumni.backend.module.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentCreateEvent {

    private final Member member;
}
