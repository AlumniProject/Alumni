package Alumni.backend.module.repository;

import Alumni.backend.module.domain.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UniversityRepository extends JpaRepository<University, Long> {
    Boolean existsByUnivEmail1(String email);

    Boolean existsByUnivEmail2(String email);
    University findByUnivEmail(String univEmail);
}
