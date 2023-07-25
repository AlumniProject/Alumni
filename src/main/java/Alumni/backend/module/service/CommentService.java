package Alumni.backend.module.service;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.Comment;
import Alumni.backend.module.domain.CommentLike;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Post;
import Alumni.backend.module.repository.CommentLikeRepository;
import Alumni.backend.module.repository.CommentRepository;
import Alumni.backend.module.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public void createComment(Member member,Long postId, String content){
        if(content.length() == 0)
            throw new IllegalArgumentException("Bad Request");

        Post post = postRepository.findById(postId).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다."));

        Comment comment = Comment.createComment(member, post, content);

        commentRepository.save(comment);

        postRepository.updateCommentCount(post.getCommentNum()+1, postId);//게시글에 달린 댓글 수 증가
    }

    public void modifyComment(Member member, Long commentId, String content){

        if(content.length() == 0)
            throw new IllegalArgumentException("Bad Request");

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 댓글입니다."));

        if(!comment.getMember().getId().equals(member.getId()))//수정하는 사람과 작성자가 같은지 확인
            throw new IllegalArgumentException("Bad Request");

        comment.modifyComment(content);
    }

    public void deleteComment(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 댓글입니다."));

        if(!comment.getMember().getId().equals(member.getId()))//수정하는 사람과 작성자가 같은지 확인
            throw new IllegalArgumentException("Bad Request");

        Post post = comment.getPost();
        int count = comment.getChildren().size() + 1;

        List<CommentLike> parentLikeList = commentLikeRepository.findByCommentId(comment.getId());
        commentLikeRepository.deleteAll(parentLikeList);

        for(int i = 0; i<count-1; i++){
            Comment children = comment.getChildren().get(i);
            List<CommentLike> childrenLikeList = commentLikeRepository.findByCommentId(children.getId());
            commentLikeRepository.deleteAll(childrenLikeList);
        }

        commentRepository.delete(comment);

        //게시글에 달린 댓글 수 감소, 자식 댓글 수 만큼도 감소해주기
        postRepository.updateCommentCount(post.getCommentNum()- count, post.getId());
    }

    public void createRecomment(Member member, Long commentId, String content) {
        if(content.length() == 0)
            throw new IllegalArgumentException("Bad Request");

        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("상위 댓글이 존재하지 않습니다."));//부모 댓글 찾기

        Post post = postRepository.findById(parent.getPost().getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다."));

        Comment recomment = Comment.createComment(member, post, content);
        recomment.setParent(parent);

        commentRepository.save(recomment);

        postRepository.updateCommentCount(post.getCommentNum()+1, parent.getPost().getId());//게시글에 달린 댓글 수 증가
    }

    public void modifyRecomment(Member member, Long commentId, String content) {
        if(content.length() == 0)
            throw new IllegalArgumentException("Bad Request");

        Comment recomment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 대댓글입니다."));

        Post post = postRepository.findById(recomment.getPost().getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글입니다."));

        if(!recomment.getMember().getId().equals(member.getId()))//수정하는 사람과 작성자가 같은지 확인
            throw new IllegalArgumentException("Bad Request");

        recomment.modifyComment(content);
    }

    public void deleteRecomment(Member member, Long commentId) {
        Comment recomment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 대댓글입니다."));

        if(!recomment.getMember().getId().equals(member.getId()))//수정하는 사람과 작성자가 같은지 확인
            throw new IllegalArgumentException("Bad Request");

        Post post = recomment.getPost();
        postRepository.updateCommentCount(post.getCommentNum()-1, post.getId());

        List<CommentLike> recommentLikeList = commentLikeRepository.findByCommentId(recomment.getId());
        commentLikeRepository.deleteAll(recommentLikeList);

        commentRepository.delete(recomment);
    }
}
