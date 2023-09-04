package Alumni.backend.module.service.community;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.community.CommentLike;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.community.PostLike;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.CommentLikeRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.community.PostLikeRepository;
import Alumni.backend.module.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final RedisService redisService;

    public String postLike(Member member, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다."));
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원입니다."));

        Optional<PostLike> byMemberAndPost = postLikeRepository.findByMemberAndPost(findMember.getId(), post.getId());
        if (byMemberAndPost.isPresent()) { // 좋아요 취소
            // post_like 테이블에서 삭제
            postLikeRepository.delete(byMemberAndPost.get());
            redisService.decrValue("post_id:" + postId + "_likes");
            return "게시글 좋아요 취소 완료";
        }
        PostLike postLike = PostLike.createPostLike(post, findMember);
        postLikeRepository.save(postLike);
        postLike.setMember(findMember);
        redisService.incrValue("post_id:" + postId + "_likes");
        return "게시글 좋아요 완료";
    }

    public String commentLike(Member member, Long commentId) {
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원입니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoExistsException("존재하지 않는 댓글입니다."));

        Optional<CommentLike> byMemberAndComment = commentLikeRepository.findByMemberAndComment(findMember.getId(), comment.getId());
        if (byMemberAndComment.isPresent()) {
            minusLike(byMemberAndComment.get(), commentId);
            return "댓글 좋아요 취소 완료";
        }
        plusLike(comment, findMember);
        return "댓글 좋아요 완료";
    }

    public String recommentLike(Member member, Long commentId) {
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원입니다."));
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("상위 댓글이 존재하지 않습니다."));//부모 댓글 찾기
        if (parent.getParent() == null)
            throw new IllegalArgumentException("Bad request");

        Optional<CommentLike> byMemberAndComment = commentLikeRepository.findByMemberAndComment(findMember.getId(), parent.getId());
        if (byMemberAndComment.isPresent()) {
            minusLike(byMemberAndComment.get(), commentId);
            return "대댓글 좋아요 취소 완료";
        }
        plusLike(parent, findMember);
        return "대댓글 좋아요 완료";
    }

    public void deleteLikesProcess(Member member) {
        List<PostLike> postLikes = member.getPostLikes();
        List<CommentLike> commentLikes = member.getCommentLikes();
        if (!postLikes.isEmpty()) {
            postLikeRepository.deleteAll(member.getPostLikes());
        }
        if (!commentLikes.isEmpty()) {
            commentLikeRepository.deleteAll(member.getCommentLikes());
        }
    }

    private void plusLike(Comment comment, Member findMember) {
        CommentLike commentLike = CommentLike.createCommentLike(comment, findMember);
        commentLikeRepository.save(commentLike);
        commentLike.setMember(findMember);
        redisService.incrValue("comment_id:" + comment.getId() + "_likes");
    }

    private void minusLike(CommentLike findCommentLike, Long commentId) {
        commentLikeRepository.delete(findCommentLike);
        redisService.decrValue("comment_id:" + commentId + "_likes");
    }
}
