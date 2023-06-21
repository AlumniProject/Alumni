package Alumni.backend.module.repository;

import Alumni.backend.module.domain.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {
    Boolean existsByUnivEmail(String email);
    University findByUnivEmail(String univEmail);
}
