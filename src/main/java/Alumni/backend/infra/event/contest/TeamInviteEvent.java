package Alumni.backend.infra.event.contest;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeamInviteEvent {
    private final Member member;
    private final Long team_id;
    private final String leaderNickname;
    private final String title;
}
