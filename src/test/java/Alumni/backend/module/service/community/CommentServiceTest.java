package Alumni.backend.module.service.community;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.community.Board;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.community.BoardRepository;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentServiceTest {
    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentService commentService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private BoardRepository boardRepository;

    @AfterEach
    @Order(1)
    void testDown1(){//대댓글 먼저 삭제 해줘야 함
        List<Comment> commentList = commentRepository.findAll();

        commentRepository.deleteAllInBatch(commentList.stream()
                .filter(comment -> comment.getParent() != null)
                .collect(Collectors.toList()));
    }

    @AfterEach
    @Order(2)
    void tearDown2() {
        commentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("댓글을 작성한다.")
    @Test
    void createComment(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        Board board = boardRepository.findById(3L).get();//기술 게시판

        Post post = Post.createPost(saveMember, board, "title", "content");
        Post savePost = postRepository.save(post);

        //when
        Long commentId = commentService.createComment(saveMember, savePost.getId(), "댓글입니다.");

        //then
        assertThat(commentId).isEqualTo(1);
        Comment comment = commentRepository.findById(commentId).get();
        assertThat(comment.getContent()).contains("댓글입니다.");
    }

    @DisplayName("대댓글을 작성한다.")
    @Test
    void createRecomment(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member1 = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember1 = memberRepository.save(member1);

        Member member2 = Member.createMember("soeun2@yu.ac.kr", "닉네임2", "20", "정보통신공학과", university, "1");
        Member saveMember2 = memberRepository.save(member2);

        Board board = boardRepository.findById(3L).get();//기술 게시판

        Post post = Post.createPost(saveMember1, board, "title", "content");
        Post savePost = postRepository.save(post);

        Comment comment = Comment.createComment(saveMember1, "부모 댓글입니다.");
        comment.setPost(savePost);
        Comment saveComment = commentRepository.save(comment);

        //when
        commentService.createRecomment(saveMember2, saveComment.getId(), "대댓글");

        //then
        List<Comment> commentList = commentRepository.findByPostId(savePost.getId());
        assertThat(commentList).hasSize(2)
                .extracting("parent", "content")
                .contains(tuple(saveComment, "대댓글"));

    }

    @DisplayName("대댓글 작성 시 상위댓글이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createRecommentWithoutParent(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member1 = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember1 = memberRepository.save(member1);

        Member member2 = Member.createMember("soeun2@yu.ac.kr", "닉네임2", "20", "정보통신공학과", university, "1");
        Member saveMember2 = memberRepository.save(member2);

        Board board = boardRepository.findById(3L).get();//기술 게시판

        Post post = Post.createPost(saveMember1, board, "title", "content");
        Post savePost = postRepository.save(post);

        //when //then
        Long parentId = 1L;
        assertThatThrownBy(() -> commentService.createRecomment(saveMember2, parentId, "대댓글"))
                .isInstanceOf(NoExistsException.class)
                .hasMessage("상위 댓글이 존재하지 않습니다");
    }
}