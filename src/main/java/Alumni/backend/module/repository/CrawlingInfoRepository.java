package Alumni.backend.module.repository;

import Alumni.backend.module.domain.CrawlingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlingInfoRepository extends JpaRepository<CrawlingInfo, Long> {
}
