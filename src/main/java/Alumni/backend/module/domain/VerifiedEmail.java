package Alumni.backend.module.domain;

import java.util.Random;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class VerifiedEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verified_email_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false)
    private boolean isVerified;

    @Column(nullable = false, length = 4)
    private String emailCode;

    //생성 메서드
    public static VerifiedEmail createVerifiedEmail(String email) {
        VerifiedEmail verifiedEmail = new VerifiedEmail();
        verifiedEmail.email = email;
        verifiedEmail.isVerified = false;
        verifiedEmail.generateEmailToken();
        return verifiedEmail;
    }

    public void generateEmailToken() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        this.emailCode = Integer.toString(randomNumber);
    }

    //인증된 이메일인 경우 isVerified -> true
    public void verifiedTrue() {
        this.isVerified = true;
    }

    public void verifiedFalse() {
        this.isVerified = false;
    }
}