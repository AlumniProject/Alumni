package Alumni.backend.module.service;

import Alumni.backend.TestData;
import Alumni.backend.module.domain.community.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Transactional
class ChatGptServiceTest {

    @Autowired
    ChatGptService chatGptService;
    @Autowired
    TestData testData;

    @Test
    @DisplayName("chat gpt api 요청 테스트")
    public void chat_gpt_test() throws Exception {
        //given
        Post post = testData.SetUpOnlyOnePost();

        //when
        String chatGptComment = chatGptService.getChatGptComment(post);

        //then
        assertNotNull(chatGptComment);
    }
}
