package Alumni.backend.module.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    //메일 전송
    public void sendMail(String toEmail, String emailCode){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setFrom("soeun8636@naver.com");
            mimeMessageHelper.setSubject("ALUMNI 이메일 인증");
            mimeMessageHelper.setText(emailCode);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
