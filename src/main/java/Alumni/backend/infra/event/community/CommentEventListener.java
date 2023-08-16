package Alumni.backend.infra.event.community;

import Alumni.backend.infra.notification.NotificationService;
import Alumni.backend.module.domain.registration.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Async
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleCommentCreateEvent(CommentCreateEvent commentCreateEvent) {
        singleMessageEvent(commentCreateEvent.getMember(), "게시글에 댓글이 작성되었습니다");
    }

    @EventListener
    public void handleRecommentCreateEvent(RecommentCreateEvent recommentCreateEvent) {
        singleMessageEvent(recommentCreateEvent.getMember(), "게시글에 대댓글이 작성되었습니다");
    }

    private void singleMessageEvent(Member member, String body) {
        String fcmToken = member.getFcmToken();
        if (!fcmToken.isBlank()) { // 로그아웃하면 fcmToken black 됨
            notificationService.sendByToken(fcmToken, "동문개발자 커뮤니티(Alumni)", body);
        }
    }
}
