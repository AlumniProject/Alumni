package Alumni.backend.module.controller;

import Alumni.backend.TestData;
import Alumni.backend.infra.principal.PrincipalDetails;
import Alumni.backend.module.domain.Member;
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

    @Test
    public void 전체_게시글_조회_테스트() throws Exception {
        Member member = testData.findMemberByEmail("1");

        mockMvc.perform(get("/post/all")
                .with(user(new PrincipalDetails(member))))
                .andDo(print());

    }
}