package Alumni.backend.module.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 4)
    private String classOf;

    @Column(nullable = false)
    private boolean agreement1;

    @Column(nullable = false)
    private boolean agreement2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image profileImage;

    @Column(nullable = false, length = 15)
    private String major;//학과

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    //생성 메서드
    public static Member createMember(String email, String nickname, String classOf,
                                      String major, University university){
        Member member = new Member();

        member.email = email;
        member.nickname = nickname;
        member.classOf = classOf;
        member.agreement1 = true;
        member.agreement2 = true;
        member.major = major;
        member.university = university;

        return member;
    }

    public void uploadProfile(Image profileImage){
        this.profileImage = profileImage;
    }
}
