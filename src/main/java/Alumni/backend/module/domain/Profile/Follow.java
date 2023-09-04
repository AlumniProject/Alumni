package Alumni.backend.module.domain.Profile;

import Alumni.backend.module.domain.registration.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member follower;//하는 사람(나)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member following;//하려는 사람(상대방)

    public static Follow createFollow(Member follower, Member following){
        Follow follow = new Follow();
        follow.follower = follower;
        follow.following = following;
        return follow;
    }
}
