package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.University;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UniversityRepository extends JpaRepository<University, Long> {

    @Query("SELECT CASE WHEN (u.univEmail1 LIKE %:email% OR u.univEmail2 LIKE %:email%) THEN true ELSE false END FROM University u WHERE u.univEmail1 LIKE %:email% OR u.univEmail2 LIKE %:email%")
    Boolean existsByEmail(@Param("email") String email);

    University findByUnivEmail1OrUnivEmail2(String univEmail1, String univEmail2);
}
