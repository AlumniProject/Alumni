package Alumni.backend.module.service.community;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.community.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.community.BoardRepository;
import Alumni.backend.module.repository.community.CommentLikeRepository;
import Alumni.backend.module.repository.community.PostLikeRepository;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@ActiveProfiles("test")
class LikeServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private LikeService likeService;
    @Autowired private PostLikeRepository postLikeRepository;

    @Autowired private CommentRepository commentRepository;
    @Autowired private CommentLikeRepository commentLikeRepository;

    @AfterEach
    @Order(1)
    void testDown1(){//대댓글 먼저 삭제 해줘야 함
        commentLikeRepository.deleteAllInBatch();//댓글 좋아요 삭제

        List<Comment> commentList = commentRepository.findAll();

        commentRepository.deleteAllInBatch(commentList.stream()
                .filter(comment -> comment.getParent() != null)
                .collect(Collectors.toList()));
    }

    @AfterEach
    @Order(2)
    void tearDown2() {
        commentRepository.deleteAllInBatch();
        postLikeRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("게시글에 좋아요를 할 수 있다.")
    @Test
    void postLike1(){
        //given
        Member member = createMember("test@yu.ac.kr", "테스트닉네임");
        Post post = createPost(1L, member);

        //when
        String message = likeService.postLike(member, post.getId());

        //then
        assertThat(message).isEqualTo("게시글 좋아요 완료");
    }

    @DisplayName("같은 게시글에 한번 더 좋아요를 누르면 좋아요가 취소된다.")
    @Test
    void postLike2(){
        //given
        Member member = createMember("test@yu.ac.kr", "테스트닉네임");

        Post post = createPost(1L, member);

        PostLike postLike = PostLike.createPostLike(post, member);
        postLikeRepository.save(postLike);

        //when
        String message = likeService.postLike(member, post.getId());

        //then
        assertThat(message).isEqualTo("게시글 좋아요 취소 완료");

    }

    @DisplayName("존재하지 않는 게시글에 좋아요를 하면 예외가 발생한다.")
    @Test
    void postLikeWithoutPost(){
        //given
        Member member = createMember("test@yu.ac.kr", "테스트닉네임");

        //when //then
        Long postId = 1L;
        assertThatThrownBy(() -> likeService.postLike(member, postId))
                .isInstanceOf(NoExistsException.class)
                .hasMessage("존재하지 않는 게시글입니다.");

    }

    @DisplayName("댓글에 좋아요를 할 수 있다.")
    @Test
    void commentLike1(){
        //given
        Member member1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member member2 = createMember("test2@yu.ac.kr", "닉네임2");

        Post post = createPost(1L, member1);

        Comment comment = createComment(member2, "댓글입니다.", post);

        //when
        String message = likeService.commentLike(member1, comment.getId());

        //then
        assertThat(message).isEqualTo("댓글 좋아요 완료");
    }

    @DisplayName("좋아요한 댓글에 좋아요를 다시 누르면 좋아요가 취소된다.")
    @Test
    void commentLike2(){
        //given
        Member member1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member member2 = createMember("test2@yu.ac.kr", "닉네임2");

        Post post = createPost(1L, member1);

        Comment comment = createComment(member2, "댓글입니다.", post);
        CommentLike commentLike = CommentLike.createCommentLike(comment, member1);
        commentLikeRepository.save(commentLike);

        //when
        String message = likeService.commentLike(member1, comment.getId());

        //then
        assertThat(message).isEqualTo("댓글 좋아요 취소 완료");
    }

    private Post createPost(long boardId, Member saveMember) {
        Board board = boardRepository.findById(boardId).get();

        Post post = Post.createPost(saveMember, board, "title", "content");
        Post savePost = postRepository.save(post);

        return savePost;
    }

    private Member createMember(String email, String nickname) {
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember(email, nickname, "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        return saveMember;
    }

    private Comment createComment(Member saveMember1, String content, Post savePost) {
        Comment comment = Comment.createComment(saveMember1, content);
        comment.setPost(savePost);
        Comment saveComment = commentRepository.save(comment);

        return saveComment;
    }
}