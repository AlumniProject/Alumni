package Alumni.backend.module.service;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.Comment;
import Alumni.backend.module.domain.CommentLike;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Post;
import Alumni.backend.module.domain.PostLike;
import Alumni.backend.module.repository.CommentLikeRepository;
import Alumni.backend.module.repository.CommentRepository;
import Alumni.backend.module.repository.MemberRepository;
import Alumni.backend.module.repository.PostLikeRepository;
import Alumni.backend.module.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

  private final PostRepository postRepository;
  private final PostLikeRepository postLikeRepository;
  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;

  public String postLike(Member member, Long postId) {
    String message = "게시글 좋아요 완료";
    Post post = postRepository.findById(postId).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다."));
    Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원입니다."));

    if (postLikeRepository.findByMemberAndPost(findMember.getId(), post.getId()).isPresent()) {
      PostLike findPostLike = postLikeRepository.findByMemberAndPost(findMember.getId(), post.getId()).get();

      postLikeRepository.delete(findPostLike);

      postRepository.updateLikeCount(post.getLikeNum()-1, post.getId());//좋아요 수 감소
      message = "게시글 좋아요 취소 완료";
      return message;
   }

    PostLike postLike = PostLike.createPostLike(post, findMember);
    postLikeRepository.save(postLike);
    postLike.setMember(findMember);

    postRepository.updateLikeCount(post.getLikeNum()+1, post.getId());//좋아요 수 증가

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

  public String recommentLike(Member member, Long commentId){
    String message = "대댓글 좋아요 완료";

    Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원입니다."));
    Comment parent = commentRepository.findById(commentId)
        .orElseThrow(() -> new NoExistsException("상위 댓글이 존재하지 않습니다."));//부모 댓글 찾기

    if(parent.getParent()==null)
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

    commentRepository.updateLikeCount(comment.getLikeNum()+1, comment.getId());
  }

  private void minusLike(Member findMember, Comment comment) {
    CommentLike findCommentLike = commentLikeRepository.findByMemberAndComment(findMember.getId(),
        comment.getId()).get();

    commentLikeRepository.delete(findCommentLike);

    commentRepository.updateLikeCount(comment.getLikeNum()-1, comment.getId());
  }
}
