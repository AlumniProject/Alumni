package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
