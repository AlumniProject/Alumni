package Alumni.backend.module.service.community;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.community.Board;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.community.CommentLike;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.community.BoardRepository;
import Alumni.backend.module.repository.community.CommentLikeRepository;
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
import java.util.Optional;
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
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("댓글을 작성한다.")
    @Test
    void createComment(){
        //given
        Member saveMember = createMember("test1@yu.ac.kr", "닉네임1");

        Post savePost = createPost(3L, saveMember);

        //when
        Long commentId = commentService.createComment(saveMember, savePost.getId(), "댓글입니다.");

        //then
        Comment comment = commentRepository.findById(commentId).get();
        assertThat(comment.getContent()).contains("댓글입니다.");
    }
    @DisplayName("댓글 작성 시 게시물이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createCommentWithoutPost(){
        //given
        Member saveMember = createMember("test1@yu.ac.kr", "닉네임1");

        //when //then
        Long postId = 1L;
        assertThatThrownBy(()->commentService.createComment(saveMember, postId, "댓글입니다."))
                .isInstanceOf(NoExistsException.class)
                .hasMessage("존재하지 않는 게시글입니다");
    }

    @DisplayName("대댓글을 작성한다.")
    @Test
    void createRecomment(){
        //given
        Member saveMember1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member saveMember2 = createMember("test2@yu.ac.kr", "닉네임2");

        Post savePost = createPost(3L, saveMember1);

        Comment saveComment = createComment(saveMember1, "부모 댓글입니다.", savePost);

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
        Member saveMember1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member saveMember2 = createMember("test2@yu.ac.kr", "닉네임2");

        createPost(3L, saveMember1);

        //when //then
        Long parentId = 1L;
        assertThatThrownBy(() -> commentService.createRecomment(saveMember2, parentId, "대댓글"))
                .isInstanceOf(NoExistsException.class)
                .hasMessage("상위 댓글이 존재하지 않습니다");
    }

    @DisplayName("댓글을 삭제한다")
    @Test
    void deleteComment(){
        //given
        Member saveMember = createMember("test1@yu.ac.kr", "닉네임1");

        Post savePost = createPost(1L, saveMember);

        Comment saveComment = createComment(saveMember, "댓글입니다.", savePost);

        //when
        commentService.deleteComment(saveMember, saveComment.getId());

        //then
        Optional<Comment> findComment = commentRepository.findById(saveComment.getId());
        assertThat(findComment.isEmpty());
    }
    @DisplayName("댓글 삭제 시 대댓글도 삭제된다.")
    @Test
    void deleteCommentWithRecomment(){
        //given
        Member saveMember1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member saveMember2 = createMember("test2@yu.ac.kr", "닉네임2");

        Post savePost = createPost(1L, saveMember1);

        Comment saveComment = createComment(saveMember1, "댓글입니다.", savePost);
        Comment saveRecomment = createRecomment(saveMember2, "대댓글입니다.", savePost, saveComment);

        //when
        commentService.deleteComment(saveMember1, saveComment.getId());

        //then
        Optional<Comment> findRecomment = commentRepository.findById(saveRecomment.getId());
        assertThat(findRecomment.isEmpty());
    }
    @DisplayName("댓글 삭제 시 댓글에 달린 좋아요도 삭제된다.")
    @Test
    void deleteCommentWithCommentLike(){
        //given
        Member saveMember1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member saveMember2 = createMember("test2@yu.ac.kr", "닉네임2");

        Post savePost = createPost(3L, saveMember1);

        Comment saveComment = createComment(saveMember1, "댓글입니다.", savePost);

        CommentLike commentLike1 = CommentLike.createCommentLike(saveComment, saveMember1);
        CommentLike commentLike2 = CommentLike.createCommentLike(saveComment, saveMember2);
        commentLikeRepository.saveAll(List.of(commentLike1, commentLike2));

        //when
        commentService.deleteComment(saveMember1, saveComment.getId());

        //then
        List<CommentLike> commentLikeList = commentLikeRepository.findByCommentId(saveComment.getId());
        assertThat(commentLikeList).isEmpty();
    }

    @DisplayName("댓글 삭제 시 삭제하려는 사람과 작성한 사람이 다르면 예외가 발생한다.")
    @Test
    void deleteCommentOtherMember(){
        //given
        Member saveMember = createMember("test1@yu.ac.kr", "닉네임1");
        Member saveOther = createMember("test2@yu.ac.kr", "닉네임2");

        Post savePost = createPost(1L, saveMember);

        Comment saveComment = createComment(saveMember, "댓글입니다.", savePost);

        //when //then
        assertThatThrownBy(()->commentService.deleteComment(saveOther, saveComment.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bad Request");
    }

    @DisplayName("없는 댓글을 삭제하면 예외가 발생한다.")
    @Test
    void deleteNoExistComment(){
        //given
        Member saveMember = createMember("test1@yu.ac.kr", "닉네임1");

        //when //then
        Long commentId = 1L;
        assertThatThrownBy(()->commentService.deleteComment(saveMember, commentId))
                .isInstanceOf(NoExistsException.class)
                .hasMessage("존재하지 않는 댓글입니다");
    }

    @DisplayName("대댓글을 삭제한다.")
    @Test
    void deleteRecomment(){
        //given
        Member saveMember1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member saveMember2 = createMember("test2@yu.ac.kr", "닉네임2");

        Post savePost = createPost(1L, saveMember1);

        Comment saveComment = createComment(saveMember1, "부모 댓글입니다.", savePost);
        Comment saveRecomment = createRecomment(saveMember2, "자식 댓글입니다.", savePost, saveComment);

        //when
        commentService.deleteRecomment(saveMember2, saveRecomment.getId());
        //then
        Optional<Comment> findRecomment = commentRepository.findById(saveRecomment.getId());
        assertThat(findRecomment).isEmpty();
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

    private Comment createRecomment(Member saveMember2, String content, Post savePost, Comment saveComment) {
        Comment recomment = Comment.createComment(saveMember2, content);
        recomment.setPost(savePost);
        recomment.setParent(saveComment);
        Comment saveRecomment = commentRepository.save(recomment);

        return saveRecomment;
    }


}