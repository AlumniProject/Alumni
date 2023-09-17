package Alumni.backend.module.dto.gpt;

import lombok.Data;

@Data
public class ChatGptCommentResponse {

    private Long postId;
    private String answer;

    public ChatGptCommentResponse(Long postId, String answer) {
        this.postId = postId;
        this.answer = answer;
    }
}
