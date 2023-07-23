package Alumni.backend.infra.Event;

import Alumni.backend.infra.Notification.NotificationService;
import Alumni.backend.module.domain.Member;
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
public class CommentEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleCommentCreateEvent(CommentCreateEvent commentCreateEvent) {
        Member member = commentCreateEvent.getMember();
        // 로그아웃한 사용자인지 확인
        String fcmToken = member.getFcmToken();
        if (!fcmToken.isBlank()) {
            notificationService.sendByToken(fcmToken, "동문개발자 커뮤니티(Alumni)", "게시글에 댓글이 작성되었습니다");
        }
        /*List<String> fcmTokenList = memberList.stream().map(Member::getFcmToken)
                .filter(fcmToken -> !fcmToken.isBlank()).collect(Collectors.toList());
        if (fcmTokenList.size() > 0) {
            notificationService.sendByTokenList(fcmTokenList);
        }*/
    }

    @EventListener
    public void handleRecommentCreateEvent(RecommentCreateEvent recommentCreateEvent) {
        Member member = recommentCreateEvent.getMember();
        // 로그아웃한 사용자인지 확인
        String fcmToken = member.getFcmToken();
        if (!fcmToken.isBlank()) {
            notificationService.sendByToken(fcmToken, "동문개발자 커뮤니티(Alumni)", "댓글에 대댓글이 작성되었습니다");
        }
    }
}
