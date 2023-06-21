package Alumni.backend.module.repository;

import Alumni.backend.module.domain.InterestField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestFieldRepository extends JpaRepository<InterestField, Long> {
    Optional<InterestField> findByFieldName(String fieldName);
}
