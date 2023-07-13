package Alumni.backend.module.service;

import Alumni.backend.module.domain.*;
import Alumni.backend.module.dto.PostResponseDto;
import Alumni.backend.module.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    PostTagRepository postTagRepository;
    @Autowired
    PostService postService;

    @Test
    public void 인기태그_조회_테스트() throws Exception {
        List<Tag> tags = tagRepository.findAll();
        for (int i = 0; i < tags.size(); i++) {
            tags.get(i).setCount(i);
        }

        List<Tag> tagList = tagRepository.findTop5ByOrderByCountDesc();
        Assertions.assertEquals(5, tagList.size());
        Assertions.assertEquals("Kotlin", tagList.get(0).getName());
    }

    @Test
    @Rollback(value = false)
    public void 전체_게시글_조회_테스트() throws Exception {
        SetUp();

        Member m1 = memberRepository.findById(1L).get();
        List<PostResponseDto> postResponseDtos = postService.findAllPosts(m1);

        //then
        Assertions.assertEquals(5, postResponseDtos.size());
    }


    private void SetUp() {
        //given
        Image image1 = new Image("1", "1", "1");
        Image image2 = new Image("2", "2", "2");
        imageRepository.saveAll(Arrays.asList(image1, image2));

        University univ1 = universityRepository.findById(1L).get();
        University univ2 = universityRepository.findById(2L).get();

        Member member1 = Member.createMember("1", "1", "1", "1", univ1, "1");
        Member member2 = Member.createMember("2", "2", "2", "2", univ2, "2");
        Member member3 = Member.createMember("3", "3", "3", "3", univ1, "3");
        memberRepository.saveAll(Arrays.asList(member1, member2, member3));
        member1.uploadProfile(image1);
        member2.uploadProfile(image2);
        //member3.uploadProfile(image1);

        Board freeBoard = boardRepository.findByName("자유");
        Board techBoard = boardRepository.findByName("기술 Q&A");
        Board univBoard = boardRepository.findByName("우리 대학");
        Board compBoard = boardRepository.findByName("공모전");
        Post post1 = Post.createPost(member1, freeBoard, "t1", "c1");
        Post post2 = Post.createPost(member2, techBoard, "t2", "c2");
        Post post3 = Post.createPost(member3, univBoard, "t3", "c3");
        Post post4 = Post.createPost(member1, univBoard, "t4", "c4");
        Post post5 = Post.createPost(member2, compBoard, "t5", "c5");
        postRepository.saveAll(Arrays.asList(post1, post2, post3, post4, post5));

        Tag pythonTag = tagRepository.findByName("python");
        Tag javaTag = tagRepository.findByName("Java");
        PostTag postTag1 = PostTag.createPostTag(post2, pythonTag);
        PostTag postTag2 = PostTag.createPostTag(post2, javaTag);
        postTagRepository.saveAll(Arrays.asList(postTag1, postTag2));
    }
}