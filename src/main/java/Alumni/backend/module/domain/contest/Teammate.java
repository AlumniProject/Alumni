package Alumni.backend.module.domain.contest;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Teammate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teammate_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Boolean approve;
}
