package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    int countByFollowerId(Long followerId);
    int countByFollowingId(Long following);
}
