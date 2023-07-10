package Alumni.backend.module.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interested {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interested_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_field_id", nullable = false)
    private InterestField interestField;

    public static Interested createInterested(Member member, InterestField interestField) {
        Interested interested = new Interested();

        interested.member = member;
        interested.interestField = interestField;

        return interested;
    }
}
