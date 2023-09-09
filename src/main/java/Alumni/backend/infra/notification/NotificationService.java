package Alumni.backend.infra.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService {

    @Value("${fcm.key.path}")
    private String fcmPrivateKeyPath;

    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    @Value("${fcm.key.project-id}")
    private String projectId;

    @PostConstruct
    public void initNotificationService() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(fcmPrivateKeyPath).getInputStream())
                            .createScoped(List.of(fireBaseScope)))
                    .setProjectId(projectId)
                    .build(); // GoogleApi 사용하기 위해서 oauth2를 이용해 인증
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            // 알림 서버가 동작하지 않는 것
            throw new RuntimeException(e.getMessage());
        }
    }

    // 여러개 메시지 알림
    public void sendByTokenList(List<String> tokenList, String title, String body, String type, Long id) {
        if (tokenList.isEmpty())
            return;
        List<Message> messages = tokenList.stream()
                .map(token -> Message.builder()
                        .putData("time", LocalDateTime.now().toString())
                        .putData("type", type)
                        .putData("id", String.valueOf(id))
                        .setNotification(new Notification(title, body))
                        .setToken(token)
                        .build()).collect(Collectors.toList());

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(failedTokens.get(i));
                    }
                }
                log.error("List of tokens are not valid fcm token : {}", failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to cloud message. error info : {}", e.getMessage());
        }
    }

    // 단일 메시지 알림
    public void sendByToken(String token, String title, String body, String type, Long id) {
        Message message = Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .putData("type", type)
                .putData("id", String.valueOf(id))
                .setNotification(new Notification(title, body))
                .setToken(token)
                .build();
        try {
            String send = FirebaseMessaging.getInstance().send(message);
            log.info(send);
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to cloud message. error info : {}", e.getMessage());
        }
    }
}
