package Alumni.backend.module.domain.registration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Alumni.backend.module.domain.BaseTimeEntity;
import Alumni.backend.module.domain.community.CommentLike;
import Alumni.backend.module.domain.Image;
import Alumni.backend.module.domain.community.PostLike;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 10, unique = true)
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

    private String refreshToken;

    @Column(nullable = false)
    private String fcmToken;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private Set<Interested> interestFields = new HashSet<>();

    //생성 메서드
    public static Member createMember(String email, String nickname, String classOf,
                                      String major, University university, String fcmToken) {
        Member member = new Member();

        member.email = email;
        member.nickname = nickname;
        member.classOf = classOf;
        member.agreement1 = true;
        member.agreement2 = true;
        member.major = major;
        member.university = university;
        member.fcmToken = fcmToken;

        return member;
    }

    public void uploadProfile(Image profileImage) {
        this.profileImage = profileImage;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void clearInterestFields() {
        this.interestFields.clear();
    }
}
