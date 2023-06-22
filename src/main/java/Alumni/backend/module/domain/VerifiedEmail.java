package Alumni.backend.module.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerifiedEmail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verified_email_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false)
    private boolean isVerified;

    @Column(nullable = false, length = 4)
    private String emailCode;

    //생성 메서드
    public static VerifiedEmail createVerifiedEmail(String email, String emailCode){
        VerifiedEmail verifiedEmail = new VerifiedEmail();

        verifiedEmail.email = email;
        verifiedEmail.isVerified = false;
        verifiedEmail.emailCode = emailCode;

        return verifiedEmail;
    }

    //인증된 이메일인 경우 isVerified -> true
    public void verifiedTrue(){
        this.isVerified = true;
    }
}