package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long>, ContestRepositoryCustom{
}
