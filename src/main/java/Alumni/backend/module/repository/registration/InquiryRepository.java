package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
