package Alumni.backend.infra.event.contest;

import Alumni.backend.infra.notification.NotificationService;
import Alumni.backend.module.domain.registration.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Async
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleTeamModifyEvent(TeamModifyEvent teamModifyEvent) {
        multiMessageEvent(teamModifyEvent.getMembers(), "팀원 모집글이 수정되었습니다");
    }

    @EventListener
    public void handleTeamDeleteEvent(TeamDeleteEvent teamDeleteEvent) {
        multiMessageEvent(teamDeleteEvent.getMembers(), "팀원 모집글이 삭제되었습니다");
    }

    @EventListener
    public void handleTeamApplyEvent(TeamApplyEvent teamApplyEvent) {
        Member member = teamApplyEvent.getMember();
        singleMessageEvent(member, "팀원 모집글에 " + member.getNickname() + "님이 지원하였습니다");
    }

    @EventListener
    public void handleTeamLeaderApproveEvent(TeamLeaderApproveEvent teamLeaderApproveEvent) {
        multiMessageEvent(teamLeaderApproveEvent.getMembers(), "팀원으로 승인되었습니다");
    }

    @EventListener
    public void handleTeamCloseEvent(TeamCloseEvent teamCloseEvent) {
        multiMessageEvent(teamCloseEvent.getMembers(), "팀 모집이 마감되었습니다");
    }

    private void multiMessageEvent(List<Member> members, String body) {
        if (!members.isEmpty()) {
            List<String> tokenList = members.stream()
                    .map(Member::getFcmToken)
                    .filter(fcmToken -> !fcmToken.isBlank()).collect(Collectors.toList()); // 로그이웃한 사용자 제외
            notificationService.sendByTokenList(tokenList, "동문개발자 커뮤니티(Alumni)", body);
        }
    }

    private void singleMessageEvent(Member member, String body) {
        String fcmToken = member.getFcmToken();
        if (!fcmToken.isBlank()) { // 로그아웃하면 fcmToken black 됨
            notificationService.sendByToken(fcmToken, "동문개발자 커뮤니티(Alumni)", body);
        }
    }
}
