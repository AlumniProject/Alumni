package Alumni.backend.module.controller;

import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.dto.community.CommentRequestDto;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import Alumni.backend.module.service.community.CommentService;
import Alumni.backend.module.service.community.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private MemberRepository memberRepository;

    @MockBean private PostService postService;
    @MockBean private CommentService commentService;

    @BeforeEach
    private void initUser(){
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember
                ("test@test1.ac.kr", "test",
                        "20", "정보통신공학과", university, "1");
        memberRepository.save(member);
    }

    @DisplayName("새로운 댓글을 생성한다.")
    @Test
    @WithUserDetails(value = "test@test1.ac.kr", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void commentCreate() throws Exception {
        //given
        CommentRequestDto commentRequest = CommentRequestDto.builder()
                .content("댓글내용").build();
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/{post_id}", 1)
                .content(objectMapper.writeValueAsString((commentRequest)))
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("댓글 작성 완료"));
    }

    @DisplayName("댓글 생성 시 댓글 내용은 필수이다.")
    @Test
    @WithUserDetails(value = "test@test1.ac.kr", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void commentCreateWithoutContent() throws Exception {
        //given
        CommentRequestDto commentRequest = CommentRequestDto.builder()
                .content("").build();
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/{post_id}", 1)
                        .content(objectMapper.writeValueAsString((commentRequest)))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("VALID_ERROR"));
    }

    @DisplayName("새로운 대댓글을 생성한다.")
    @Test
    @WithUserDetails(value = "test@test1.ac.kr", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void recommentCreate() throws Exception {
        //given
        CommentRequestDto commentRequest = CommentRequestDto.builder()
                .content("대댓글내용").build();
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/recomment/{comment_id}", 1)
                        .content(objectMapper.writeValueAsString((commentRequest)))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("대댓글 작성 완료"));

    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    @WithUserDetails(value = "test@test1.ac.kr", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteComment() throws Exception {
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{comment_id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("댓글 삭제 완료"));
    }

    @DisplayName("대댓글을 삭제한다.")
    @Test
    @WithUserDetails(value = "test@test1.ac.kr", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteRecomment() throws Exception {
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/recomment/{comment_id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("대댓글 삭제 완료"));
    }

}