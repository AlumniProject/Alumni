package Alumni.backend.module.service.community;

import Alumni.backend.infra.event.community.CommentCreateEvent;
import Alumni.backend.infra.event.community.RecommentCreateEvent;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.community.CommentLike;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.repository.community.CommentLikeRepository;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.contest.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TeamRepository teamRepository;

    /**
     * 게시글 댓글 관련 메소드
     */
    public Long createComment(Member member, Long postId, String content) {
        if (content.isBlank())
            throw new IllegalArgumentException("Bad Request");

        Post post = postRepository.findByIdFetchJoinMemberAndImage(postId);
        if (post == null) {
            throw new NoExistsException("존재하지 않는 게시글입니다");
        }
        Comment comment = Comment.createComment(member, content);
        comment.setPost(post);
        Comment saveComment = commentRepository.save(comment);
        // Async 알림 전송
        eventPublisher.publishEvent(new CommentCreateEvent(post.getMember(), post));
        return saveComment.getId();
    }


    public void deleteComment(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 댓글입니다"));

        if (!comment.getMember().getId().equals(member.getId()))//수정하는 사람과 작성자가 같은지 확인
            throw new IllegalArgumentException("Bad Request");

        Post post = comment.getPost();
        int count = comment.getChildren().size() + 1;

        List<CommentLike> parentLikeList = commentLikeRepository.findByCommentId(comment.getId());
        commentLikeRepository.deleteAll(parentLikeList);

        for (int i = 0; i < count - 1; i++) {
            Comment children = comment.getChildren().get(i);
            List<CommentLike> childrenLikeList = commentLikeRepository.findByCommentId(children.getId());
            commentLikeRepository.deleteAll(childrenLikeList);
        }

        commentRepository.delete(comment);
    }

    public void createRecomment(Member member, Long commentId, String content) {
        if (content.isBlank())
            throw new IllegalArgumentException("Bad Request");

        Comment parent = commentRepository.findByIdAndMemberFetchJoin(commentId);
        if (parent == null) {
            throw new NoExistsException("상위 댓글이 존재하지 않습니다");
        }
        Post post = postRepository.findById(parent.getPost().getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다"));
        Comment recomment = Comment.createComment(member, content);
        recomment.setPost(post);
        recomment.setParent(parent);
        commentRepository.save(recomment);

        // Async 알림 전송
        eventPublisher.publishEvent(new RecommentCreateEvent(parent.getMember(), post));
    }

    public void deleteRecomment(Member member, Long commentId) {
        Comment recomment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 대댓글입니다"));

        if (!recomment.getMember().getId().equals(member.getId()))//수정하는 사람과 작성자가 같은지 확인
            throw new IllegalArgumentException("Bad Request");

        Post post = recomment.getPost();

        List<CommentLike> recommentLikeList = commentLikeRepository.findByCommentId(recomment.getId());
        commentLikeRepository.deleteAll(recommentLikeList);

        commentRepository.delete(recomment);
    }

    /**
     * 공모전 팀 댓글 관련 메소드
     */
    public void createTeamComment(Member member, Long teamId, String content) {
        if (content.isBlank())
            throw new IllegalArgumentException("Bad Request");

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new NoExistsException("존재하지 않는 팀"));

        Comment teamComment = Comment.createComment(member, content);
        teamComment.setTeam(team);

        commentRepository.save(teamComment);
    }

    public void deleteTeamComment(Member member, Long commentId) {
        Comment teamComment = commentRepository.findById(commentId).orElseThrow(() -> new NoExistsException("존재하지 않는 댓글"));

        if (!member.getId().equals(teamComment.getMember().getId())) //동일한 사람인지 확인
            throw new IllegalArgumentException("Bad Request");

        commentRepository.delete(teamComment);
    }

    public void createTeamRecomment(Member member, Long commentId, String content) {
        if (content.isBlank())
            throw new IllegalArgumentException("Bad Request");

        Comment parentComment = commentRepository.findById(commentId).orElseThrow(() -> new NoExistsException("상위 댓글이 존재하지 않습니다"));
        Comment recomment = Comment.createComment(member, content);
        recomment.setTeam(parentComment.getTeam());
        recomment.setParent(parentComment);

        commentRepository.save(recomment);
    }

    public void deleteTeamRecomment(Member member, Long commentId) {
        Comment recomment = commentRepository.findById(commentId).orElseThrow(() -> new NoExistsException("존재하지 않는 댓글"));

        if (!member.getId().equals(recomment.getMember().getId())) // 동일한 사람인지 확인
            throw new IllegalArgumentException("Bad Request");

        commentRepository.delete(recomment);
    }
}
