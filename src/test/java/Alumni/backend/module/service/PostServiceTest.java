package Alumni.backend.module.service;

import Alumni.backend.TestData;
import Alumni.backend.infra.response.PostSearchResponse;
import Alumni.backend.module.domain.*;
import Alumni.backend.module.dto.PostResponseDto;
import Alumni.backend.module.dto.requestDto.PostCreateRequestDto;
import Alumni.backend.module.dto.requestDto.PostModifyRequestDto;
import Alumni.backend.module.dto.requestDto.PostSearch;
import Alumni.backend.module.repository.*;
import Alumni.backend.module.repository.Post.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    TestData testData;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    /*@Test
    public void 인기태그_조회_테스트() throws Exception {
        List<Tag> tags = tagRepository.findAll();
        for (int i = 0; i < tags.size(); i++) {
            tags.get(i).setCount(i);
        }

        List<Tag> tagList = tagRepository.findTop5ByOrderByCountDesc();
        Assertions.assertEquals(5, tagList.size());
        Assertions.assertEquals("Kotlin", tagList.get(0).getName());
    }*/

    /*@Test
    //@Rollback(value = false)
    public void 전체_게시글_조회_테스트() throws Exception {
        testData.SetUp();

        Member m1 = memberRepository.findByEmail("1").get();
        List<PostResponseDto> postResponseDtos = postService.findAllPosts(m1);

        //then
        Assertions.assertEquals(5, postResponseDtos.size());
    }*/

    @Test
    public void 게시글_검색_테스트() throws Exception {
        testData.SetUp();

        PostSearch postSearch = new PostSearch();
        postSearch.setId(1L);
        Member m1 = memberRepository.findByEmail("1").get();

        PostSearchResponse<?> search = postService.search(m1, postSearch);

        //then
        Assertions.assertEquals(1, search.getCount());
    }

    @Test
    public void 게시글_등록_테스트() throws Exception {
        testData.SetUpOnlyMember();
        Member m1 = memberRepository.findByEmail("1").get();

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setBoardId(3L);
        postCreateRequestDto.setTitle("title1");
        postCreateRequestDto.setContent("content1");
        postCreateRequestDto.setHashTag(Arrays.asList("python", "Java", "AI"));

        postService.postCreate(m1, postCreateRequestDto);

        Post post = postRepository.findByTitle("title1")
                .orElseThrow(() -> new IllegalArgumentException("e"));
        Set<PostTag> postTags = post.getPostTags();

        Assertions.assertEquals(3, postTags.size());
    }

    @Test
    public void 게시글_등록_테스트2() throws Exception {
        testData.SetUpOnlyMember();
        Member m1 = memberRepository.findByEmail("1").get();

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setBoardId(5L);
        postCreateRequestDto.setTitle("title1");
        postCreateRequestDto.setContent("content1");
        postCreateRequestDto.setHashTag(List.of());

        postService.postCreate(m1, postCreateRequestDto);

        Post post = postRepository.findByTitle("title1")
                .orElseThrow(() -> new IllegalArgumentException("e"));
        Set<PostTag> postTags = post.getPostTags();

        Assertions.assertEquals(0, postTags.size());
    }

    @Test
    @DisplayName("게시글 수정 - 해시태그 삭제 시 post의 hashTag set에서도 삭제되는지 확인")
    public void 게시글_수정_테스트() throws Exception {
        testData.SetUpOnlyMember();
        Member m1 = memberRepository.findByEmail("1").get();

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setBoardId(3L);
        postCreateRequestDto.setTitle("title1");
        postCreateRequestDto.setContent("content1");
        postCreateRequestDto.setHashTag(Arrays.asList("python", "Java", "AI"));

        postService.postCreate(m1, postCreateRequestDto);

        Post post = postRepository.findByTitle("title1")
                .orElseThrow(() -> new IllegalArgumentException("e"));

        PostModifyRequestDto postModifyRequestDto = new PostModifyRequestDto();
        postModifyRequestDto.setTitle("t1");
        postModifyRequestDto.setContent("c1");
        postModifyRequestDto.setHashTag(List.of("Java"));
        postService.postModify(m1, post.getId(), postModifyRequestDto);

        postRepository.findByTitle("t1")
                .orElseThrow(() -> new IllegalArgumentException("e"));
        Set<PostTag> postTags = post.getPostTags();
        Assertions.assertEquals(1, postTags.size());
    }
}