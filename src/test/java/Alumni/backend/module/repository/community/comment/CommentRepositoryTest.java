package Alumni.backend.module.repository.community.comment;

import Alumni.backend.module.domain.community.Board;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.community.BoardRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import org.junit.jupiter.api.AfterEach;
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
class CommentRepositoryTest {
    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
    }

    @DisplayName("게시글 아이디로 달린 댓글을 찾는다")
    @Test
    void findByPostId(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member1 = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember1 = memberRepository.save(member1);
        Member member2 = Member.createMember("soeun2@yu.ac.kr", "닉네임2", "20", "정보통신공학과", university, "1");
        Member saveMember2 = memberRepository.save(member2);
        Member member3 = Member.createMember("soeun3@yu.ac.kr", "닉네임3", "20", "정보통신공학과", university, "1");
        Member saveMember3 = memberRepository.save(member2);


        Board board = boardRepository.findById(1L).get();//기술 게시판

        Post post = Post.createPost(saveMember1, board, "title", "content");
        Post savePost = postRepository.save(post);

        Comment comment1 = Comment.createComment(saveMember2, "댓글1");
        comment1.setPost(savePost);

        Comment comment2 = Comment.createComment(saveMember3, "댓글2");
        comment2.setPost(savePost);

        commentRepository.saveAll(List.of(comment1, comment2));

        //when
        List<Comment> commentList = commentRepository.findByPostId(savePost.getId());

        //then
        assertThat(commentList).hasSize(2)
                .extracting("member", "content")
                .containsExactlyInAnyOrder(tuple(saveMember2, "댓글1"),
                        tuple(saveMember3, "댓글2"));
    }
}