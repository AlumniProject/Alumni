package Alumni.backend.module.controller;

import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import Alumni.backend.module.service.community.LikeService;
import Alumni.backend.module.service.community.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LikeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private UniversityRepository universityRepository;

    @MockBean private LikeService likeService;
    @MockBean private PostService postService;

    @BeforeEach
    private void initUser(){
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember
                ("test@test1.ac.kr", "test",
                        "20", "정보통신공학과", university, "1");
        memberRepository.save(member);
    }


    @DisplayName("게시글에 좋아요를 한다.")
    @Test
    @WithUserDetails(value = "test@test1.ac.kr", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void postLike() throws Exception {
        //when //then
        mockMvc.perform(post("/post/like/{post_id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

}