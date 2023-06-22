package Alumni.backend.module.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry { //문의 table
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;//문의 내용

    public static Inquiry createInquiry(String content){
        Inquiry inquiry = new Inquiry();

        inquiry.content = content;

        return inquiry;
    }
}
