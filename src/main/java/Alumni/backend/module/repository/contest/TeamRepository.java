package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
