package Alumni.backend.module.service;

import Alumni.backend.infra.config.ChatGptConfig;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.dto.gpt.ChatGptCommentResponse;
import Alumni.backend.module.dto.gpt.ChatGptRequestDto;
import Alumni.backend.module.repository.community.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    @Value("${chat-gpt.api-key}")
    private String apiKey;
    private final PostRepository postRepository;

    public ChatGptCommentResponse getChatGptComment(Long postId) throws ParseException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoExistsException("존재하지 않는 게시글"));
        StringBuffer sb = new StringBuffer();
        try {
            requestInfo(sb, post.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = sb.toString();
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(result);
        // choices 키를 가지고 데이터를 파싱
        JSONArray choices = (JSONArray) obj.get("choices");
        JSONObject jsonObject = (JSONObject) choices.get(0);
        JSONObject message = (JSONObject) jsonObject.get("message");
        String answer = (String) message.get("content");
        return new ChatGptCommentResponse(postId, answer);
    }

    private void requestInfo(StringBuffer sb, String content) throws IOException {
        ChatGptRequestDto chatGptRequestDto = new ChatGptRequestDto(ChatGptConfig.MODEL, ChatGptConfig.MAX_TOKEN,
                ChatGptConfig.TEMPERATURE, content);

        URL url = new URL(ChatGptConfig.URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", ChatGptConfig.MEDIA_TYPE);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty(ChatGptConfig.HEADER_GPT, ChatGptConfig.GPT_PREFIX + apiKey);
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = chatGptRequestDto.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader br;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        conn.disconnect();
    }
}