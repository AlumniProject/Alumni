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
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
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

    @DisplayName("게시글 아이디로 달린 댓글을 찾는다")
    @Test
    void findByPostId(){
        //given
        Member saveMember1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member saveMember2 = createMember("test2@yu.ac.kr", "닉네임2");
        Member saveMember3 = createMember("test3@yu.ac.kr", "닉네임3");


        Post savePost = createPost(1L, saveMember1);

        Comment comment1 = createComment(saveMember2, "댓글1", savePost);

        Comment comment2 = createComment(saveMember3, "댓글2", savePost);

        commentRepository.saveAll(List.of(comment1, comment2));

        //when
        List<Comment> commentList = commentRepository.findByPostId(savePost.getId());

        //then
        assertThat(commentList).hasSize(2)
                .extracting("member", "content")
                .containsExactlyInAnyOrder(tuple(saveMember2, "댓글1"),
                        tuple(saveMember3, "댓글2"));
    }

    private Post createPost(long boardId, Member saveMember) {
        Board board = boardRepository.findById(boardId).get();

        Post post = Post.createPost(saveMember, board, "title", "content");
        Post savePost = postRepository.save(post);

        return savePost;
    }

    private Comment createComment(Member saveMember1, String content, Post savePost) {
        Comment comment = Comment.createComment(saveMember1, content);
        comment.setPost(savePost);
        Comment saveComment = commentRepository.save(comment);

        return saveComment;
    }

    private Member createMember(String email, String nickname) {
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember(email, nickname, "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        return saveMember;
    }
}