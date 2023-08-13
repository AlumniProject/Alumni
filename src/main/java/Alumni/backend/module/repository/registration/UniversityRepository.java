package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.University;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UniversityRepository extends JpaRepository<University, Long> {
    Boolean existsByUnivEmail1(String email);

    Boolean existsByUnivEmail2(String email);

    University findByUnivEmail1OrUnivEmail2(String univEmail1, String univEmail2);
}
