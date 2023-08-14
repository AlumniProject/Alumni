package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<List<Team>> findByContestId(Long contestId);
}
