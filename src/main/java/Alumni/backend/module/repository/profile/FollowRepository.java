package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    @Query("select f.following.id from Follow f where f.follower.id = :memberId")
    List<Long> findByFollowerId(@Param("memberId") Long followerId);

    int countByFollowerId(Long followerId);
    int countByFollowingId(Long following);
}
