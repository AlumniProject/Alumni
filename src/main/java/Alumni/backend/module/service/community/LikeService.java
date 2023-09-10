package Alumni.backend.module.service.community;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.community.CommentLike;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.domain.contest.ContestLike;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.community.PostLike;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.CommentLikeRepository;
import Alumni.backend.module.repository.contest.ContestLikeRepository;
import Alumni.backend.module.repository.contest.ContestRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.community.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ContestRepository contestRepository;
    private final ContestLikeRepository contestLikeRepository;

    public String postLike(Member member, Long postId) {
        String message = "게시글 좋아요 완료";
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다."));
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원입니다."));

        if (postLikeRepository.findByMemberAndPost(findMember.getId(), post.getId()).isPresent()) {
            PostLike findPostLike = postLikeRepository.findByMemberAndPost(findMember.getId(), post.getId()).get();

            postLikeRepository.delete(findPostLike);

            message = "게시글 좋아요 취소 완료";
            return message;
        }

        PostLike postLike = PostLike.createPostLike(post, findMember);
        postLikeRepository.save(postLike);
        postLike.setMember(findMember);


        return message;
    }

    public String commentLike(Member member, Long commentId) {
        String message = "댓글 좋아요 완료";

        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원입니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoExistsException("존재하지 않는 댓글입니다."));
        Post post = postRepository.findById(comment.getPost().getId())
                .orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다."));

        if (commentLikeRepository.findByMemberAndComment(findMember.getId(), comment.getId()).isPresent()) {
            minusLike(findMember, comment);
            message = "댓글 좋아요 취소 완료";
            return message;
        }

        plusLike(comment, findMember);

        return message;
    }

    public String recommentLike(Member member, Long commentId) {
        String message = "대댓글 좋아요 완료";

        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원입니다."));
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("상위 댓글이 존재하지 않습니다."));//부모 댓글 찾기

        if (parent.getParent() == null)
            throw new IllegalArgumentException("Bad request");

        Post post = postRepository.findById(parent.getPost().getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다."));

        if (commentLikeRepository.findByMemberAndComment(findMember.getId(), parent.getId()).isPresent()) {

            minusLike(findMember, parent);
            message = "대댓글 좋아요 취소 완료";
            return message;
        }

        plusLike(parent, findMember);

        return message;
    }

    private void plusLike(Comment comment, Member findMember) {
        CommentLike commentLike = CommentLike.createCommentLike(comment, findMember);
        commentLikeRepository.save(commentLike);
        commentLike.setMember(findMember);

    }

    private void minusLike(Member findMember, Comment comment) {
        CommentLike findCommentLike = commentLikeRepository.findByMemberAndComment(findMember.getId(),
                comment.getId()).get();

        commentLikeRepository.delete(findCommentLike);

    }

    public String contestLike(Member member, Long contestId) {

        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NoExistsException("존재하지 않는 공모전"));

        if(contestLikeRepository.findByMemberIdAndContestId(member.getId(), contestId).isPresent()){
            ContestLike contestLike = contestLikeRepository.findByMemberIdAndContestId(member.getId(), contestId).get();
            contestLikeRepository.delete(contestLike);

            return "공모전 좋아요 취소 완료";
        }

        ContestLike contestLike = ContestLike.createContestLike(member, contest);
        contestLikeRepository.save(contestLike);


        return "공모전 좋아요 완료";
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
}
