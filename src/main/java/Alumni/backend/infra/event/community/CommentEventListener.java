package Alumni.backend.infra.event.community;

import Alumni.backend.infra.notification.NotificationService;
import Alumni.backend.module.domain.community.Post;
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
        Post post = commentCreateEvent.getPost();
        singleMessageEvent(commentCreateEvent.getMember(),
                "본인이 작성한 글 <" + post.getTitle() + "> 에 새로운 댓글이 달렸어요!",
                post.getId());
    }

    @EventListener
    public void handleRecommentCreateEvent(RecommentCreateEvent recommentCreateEvent) {
        Post post = recommentCreateEvent.getPost();
        singleMessageEvent(recommentCreateEvent.getMember(),
                "본인이 작성한 댓글에 새로운 대댓글이 달렸어요!",
                post.getId());
    }

    private void singleMessageEvent(Member member, String body, Long postId) {
        String fcmToken = member.getFcmToken();
        if (!fcmToken.isBlank()) { // 로그아웃하면 fcmToken blank 됨
            notificationService.sendByToken(fcmToken, "동문개발자 커뮤니티(Alumni)", body, "0", postId);
        }
    }
}
