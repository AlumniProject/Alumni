package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Interested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface InterestedRepository extends JpaRepository<Interested, Long> {
    List<Interested> findByMemberId(Long memberId);
}