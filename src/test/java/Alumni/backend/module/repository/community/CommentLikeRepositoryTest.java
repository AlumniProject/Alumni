package Alumni.backend.module.repository.community;


import Alumni.backend.module.domain.community.Board;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.community.CommentLike;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentLikeRepositoryTest {
    @Autowired private CommentLikeRepository commentLikeRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private CommentRepository commentRepository;

    @DisplayName("댓글 아이디로 댓글에 달린 좋아요를 찾는다")
    @Test
    void findByCommentId(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member1 = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember1 = memberRepository.save(member1);
        Member member2 = Member.createMember("soeun2@yu.ac.kr", "닉네임2", "20", "정보통신공학과", university, "1");
        Member saveMember2 = memberRepository.save(member2);

        Board board = boardRepository.findById(3L).get();

        Post post = Post.createPost(saveMember1, board, "title", "content");
        Post savePost = postRepository.save(post);
        Comment comment = Comment.createComment(saveMember1, "댓글1");
        comment.setPost(savePost);
        Comment saveComment = commentRepository.save(comment);

        CommentLike commentLike1 = CommentLike.createCommentLike(saveComment, saveMember1);
        CommentLike commentLike2 = CommentLike.createCommentLike(saveComment, saveMember2);

        commentLikeRepository.saveAll(List.of(commentLike1, commentLike2));

        //when
        List<CommentLike> commentLikeList = commentLikeRepository.findByCommentId(saveComment.getId());

        //then
        assertThat(commentLikeList).hasSize(2)
                .extracting("comment", "member")
                .containsExactlyInAnyOrder(tuple(saveComment, saveMember1),
                        tuple(saveComment, saveMember2));

    }
}