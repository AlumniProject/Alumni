package Alumni.backend.module.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptService {
    private final ChatgptService chatgptService;

    public String getChatResponse(String prompt) {
        // ChatGPT 에게 질문을 던집니다.
        String responseMessage = chatgptService.sendMessage(prompt);
        return responseMessage;
    }
}