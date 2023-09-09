package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.ContestLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContestLikeRepository extends JpaRepository<ContestLike, Long> {
    Optional<ContestLike> findByMemberIdAndContestId(Long memberId, Long contestId);
}
