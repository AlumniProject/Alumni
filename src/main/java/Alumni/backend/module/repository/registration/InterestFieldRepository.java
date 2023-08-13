package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.InterestField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface InterestFieldRepository extends JpaRepository<InterestField, Long> {
    /*@Query("select I from InterestField I where I.fieldName = :fieldName")
    Optional<InterestField> findByFieldName(@Param("fieldName") String fieldName);*/

    Optional<InterestField> findByFieldName(String fieldName);
}
