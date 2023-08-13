package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.VerifiedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface VerifiedEmailRepository extends JpaRepository<VerifiedEmail, Long> {
    Boolean existsByEmail(String email);
    Optional<VerifiedEmail> findByEmail(String email);
}