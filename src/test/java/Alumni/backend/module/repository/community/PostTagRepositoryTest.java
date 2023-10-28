package Alumni.backend.module.repository.community;

import Alumni.backend.module.domain.community.Board;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.community.PostTag;
import Alumni.backend.module.domain.community.Tag;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostTagRepositoryTest {
    @Autowired private PostTagRepository postTagRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private TagRepository tagRepository;

    @AfterEach
    void tearDown() {
        postTagRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("게시글 아이디로 태그를 찾는다")
    @Test
    void findByPostId(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        Board board = boardRepository.findById(3L).get();

        Post post = Post.createPost(saveMember, board, "title", "content");
        Post savePost = postRepository.save(post);

        Tag tag1 = tagRepository.findByName("python").get();
        Tag tag2 = tagRepository.findByName("Java").get();
        Tag tag3 = tagRepository.findByName("AI").get();

        PostTag postTag1 = PostTag.createPostTag(savePost, tag1);
        PostTag postTag2 = PostTag.createPostTag(savePost, tag2);
        PostTag postTag3 = PostTag.createPostTag(savePost, tag3);

        postTagRepository.saveAll(List.of(postTag1, postTag2, postTag3));

        //when
        List<PostTag> postTagList = postTagRepository.findByPostId(savePost.getId());

        //then
        assertThat(postTagList).hasSize(3)
                .extracting("post", "tag")
                .containsExactlyInAnyOrder(tuple(savePost, tag1),
                        tuple(savePost, tag2),
                        tuple(savePost, tag3));
    }
}