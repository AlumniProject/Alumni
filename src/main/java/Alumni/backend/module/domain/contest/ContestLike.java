package Alumni.backend.module.domain.contest;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class ContestLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_like_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
