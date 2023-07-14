package Alumni.backend.module.service;

import Alumni.backend.TestData;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.PostSearchResponse;
import Alumni.backend.module.domain.*;
import Alumni.backend.module.dto.PostResponseDto;
import Alumni.backend.module.dto.PostSearch;
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
    TestData testData;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    MemberRepository memberRepository;
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
    //@Rollback(value = false)
    public void 전체_게시글_조회_테스트() throws Exception {
        testData.SetUp();

        Member m1 = memberRepository.findByEmail("1").get();
        List<PostResponseDto> postResponseDtos = postService.findAllPosts(m1);

        //then
        Assertions.assertEquals(5, postResponseDtos.size());
    }

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
}