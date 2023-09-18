package Alumni.backend.module.service;

import Alumni.backend.infra.config.ChatGptConfig;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.Image;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.dto.community.CommentDto;
import Alumni.backend.module.dto.community.RecommentDto;
import Alumni.backend.module.dto.gpt.ChatGptRequestDto;
import Alumni.backend.module.repository.ImageRepository;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@DependsOn("universityService") // myService1 빈을 초기화 후에 초기화
@RequiredArgsConstructor
@Transactional
public class ChatGptService {

    @Value("${chat-gpt.api-key}")
    private String apiKey;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;
    private final FileService fileService;
    private final ImageRepository imageRepository;
    @PostConstruct
    public void chatGptSignUp() throws IOException {
        if (!memberRepository.findByNickname("chatgpt").isPresent()) {//지피티 없으면 회원가입
            University university = universityRepository.findById(14L).get();//그냥 서울대학교로 함

            Member member = Member.createMember("chatgpt@test.ac.kr", "chatgpt",
                    "22", "OpenAI", university, "token");

            String storageImageName = "chatgpt.png";
            imageRepository.save(new Image("chatgpt.png", storageImageName, fileService.getFileUrl(storageImageName)));

            memberRepository.save(member);
        }
    }

    public CommentDto getChatGptComment(Long postId) throws ParseException {
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

        Member member = memberRepository.findByNickname("chatgpt").get();//닉네임으로 찾기
        //댓글로 저장
        Comment comment = Comment.createComment(member, answer);
        comment.setPost(post);
        Comment saveComment = commentRepository.save(comment);

        CommentDto commentDto = CommentDto.getCommentDto(saveComment, 0L);
        List<RecommentDto> recommentDtos =new ArrayList<>();
        commentDto.setRecommentList(recommentDtos);

        return commentDto;
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