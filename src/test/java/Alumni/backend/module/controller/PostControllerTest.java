package Alumni.backend.module.controller;

import Alumni.backend.TestData;
import Alumni.backend.infra.principal.PrincipalDetails;
import Alumni.backend.module.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestData testData;

    /**
     * testDate의 setUp 이용하여 dummy data 생성할 것
     */
    @BeforeEach
    void beforeEach() {
        testData.SetUp();
    }

    @Test
    public void 전체_게시글_조회_테스트() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/all")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 ID로만 게시글 검색")
    public void 게시글_검색_테스트() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/search?id=4")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 ID + 내용으로 게시글 검색")
    public void 게시글_검색_테스트2() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/search?id=1&content=1")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("기술게시판 게시글 검색")
    public void 게시글_검색_테스트3() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/search?id=3&hashTag=Java")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("기술게시판 게시글 검색 - 없는 해시태그")
    public void 게시글_검색_테스트4() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/search?id=3&hashTag=algorithm")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("우리 대학 게시판 게시글 검색")
    public void 게시글_검색_테스트5() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/search?id=2&content=c")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("우리 대학 게시판 게시글 검색 - 다른 대학의 회윈")
    public void 게시글_검색_테스트6() throws Exception {
        Member member = testData.findMemberByEmail("2");

        mockMvc.perform(get("/post/search?id=2&content=c")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("기술 게시글 검색 - 해시태그 여러개")
    public void 게시글_검색_테스트7() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/search?id=3&hashTag=python,Kotlin&content=2")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("기술 게시글 아닌 경우에 hashTag 있으면 에러")
    public void 게시글_검색_테스트8() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/search?id=2&hashTag=python,Java")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("hashTag가 5개 초과하는 경우 에러")
    public void 게시글_검색_테스트9() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/search?id=2&hashTag=python,Java,Kotlin,Algorithm,spring,flutter")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    public void 게시글_상세조회_테스트() throws Exception {
        //testData.SetUpComment();
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/1")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글")
    public void 게시글_상세조회_테스트2() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/6")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }

    @Test
    public void 모든_해시태그_조회() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/tag/all")
                        .with(user(new PrincipalDetails(member))))
                .andDo(print());
    }
}