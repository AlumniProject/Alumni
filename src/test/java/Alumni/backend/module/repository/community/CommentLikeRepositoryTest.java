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
import java.util.Optional;

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
    @DisplayName("댓글 아이디로 댓글에 달린 좋아요를 찾는다")
    @Test
    void findByCommentId(){
        //given
        Member saveMember1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member saveMember2 = createMember("test2@yu.ac.kr", "닉네임2");

        Post savePost = createPost(3L, saveMember1);

        Comment saveComment = createComment(saveMember1, "댓글1", savePost);

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

    @DisplayName("회원 아이디와 댓글 아이디로 CommentLike 찾기")
    @Test
    void findByMemberAndComment(){
        //given
        Member saveMember = createMember("test1@yu.ac.kr", "닉네임1");

        Post savePost = createPost(1L, saveMember);

        Comment saveComment = createComment(saveMember, "댓글1", savePost);

        CommentLike commentLike = CommentLike.createCommentLike(saveComment, saveMember);

        commentLikeRepository.save(commentLike);

        //when
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByMemberAndComment(saveMember.getId(), saveComment.getId());

        //then
        assertThat(findCommentLike).isNotEmpty();

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