package Alumni.backend.module.repository.Contest;

import Alumni.backend.module.domain.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long>, ContestRepositoryCustom{
}
