package Alumni.backend.module.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    //메일 내용
    /*public MimeMessage createMailForm(String email) throws MessagingException, UnsupportedEncodingException {
        String setFrom = "soeun8636@naver.com";
        String toEmail = email;
        String title = "ALUMNI";

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //메일을 받는 사용자
        message.setFrom(setFrom);//보내는 사람
        message.setSubject(title);//제목


        message.setText(setContext(emailCode), "utf-8", "html");

        return message;
    }

    //메일 전송
    public String sendMail(String toEmail){
        //메일 전송에 필요한 정보 설정
        MimeMessage emailForm = null;

        try {
            emailForm = createMailForm(toEmail);
        } catch(Exception e){
            throw new RuntimeException("INTERNAL SERVER ERROR");
        }

        //실제 메일 전송
        mailSender.send(emailForm);

        return emailCode;//인증코드 반환
    }

    public String setContext(String emailCode){
        Context context = new Context();
        context.setVariable("emailCode", emailCode);
        return templateEngine.process("mail", context);
    }*/
}
