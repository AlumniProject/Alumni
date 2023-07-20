package Alumni.backend.module.service;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.Comment;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Post;
import Alumni.backend.module.repository.CommentRepository;
import Alumni.backend.module.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void createComment(Member member,Long postId, String content){
        if(content.length() == 0)
            throw new IllegalArgumentException("Bad Request");

        Post post = postRepository.findById(postId).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글 입니다."));

        post.setCommentNum(post.getCommentNum() + 1);//댓글 수 증가

        Comment comment = Comment.createComment(member, post, content);

        commentRepository.save(comment);
    }

    public void modifyComment(Member member, Long commentId, String content){

        if(content.length() == 0)
            throw new IllegalArgumentException("Bad Request");

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 댓글 입니다."));

        if(!comment.getMember().getId().equals(member.getId()))//수정하는 사람과 작성자가 같은지 확인
            throw new IllegalArgumentException("Bad Request");

        comment.modifyComment(content);
    }

    public void DeleteComment(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoExistsException("존재하지 않는 댓글 입니다."));

        if(!comment.getMember().getId().equals(member.getId()))//삭제하는 사람과 작성자가 같은지 확인
            throw new IllegalArgumentException("Bad Request");

        Post post = comment.getPost();
        post.setCommentNum(post.getCommentNum() - 1);//댓글 수 감소

        commentRepository.delete(comment);
    }
}
