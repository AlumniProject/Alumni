package Alumni.backend.infra.event.community;

import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentCreateEvent {

    private final Member member;
    private final Post post;
}
