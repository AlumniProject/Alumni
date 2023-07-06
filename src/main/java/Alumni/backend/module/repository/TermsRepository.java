package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TermsRepository extends JpaRepository<Terms, Long> {
}