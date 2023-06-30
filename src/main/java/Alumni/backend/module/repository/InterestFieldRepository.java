package Alumni.backend.module.repository;

import Alumni.backend.module.domain.InterestField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterestFieldRepository extends JpaRepository<InterestField, Long> {
    @Query("select I from InterestField I where I.fieldName = :fieldName")
    Optional<InterestField> findByFieldName(@Param("fieldName") String fieldName);
}
