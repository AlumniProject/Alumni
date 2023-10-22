package Alumni.backend.module.repository.community;

import Alumni.backend.module.domain.community.Board;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.community.PostLike;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class PostLikeRepositoryTest {
    @Autowired private PostLikeRepository postLikeRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UniversityRepository universityRepository;

    @DisplayName("멤버와 게시글로 좋아요를 찾을 수 있다.")
    @Test
    void findByMemberAndPost(){
        //given
        Member member = createMember("test@yu.ac.kr", "test");
        Post post = createPost(1L, member);

        PostLike postLike = PostLike.createPostLike(post, member);
        postLikeRepository.save(postLike);

        //when
        Optional<PostLike> find = postLikeRepository.findByMemberAndPost(member.getId(), post.getId());

        //then
        Assertions.assertThat(find).isPresent();

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
}