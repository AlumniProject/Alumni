package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Interested;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestedRepository extends JpaRepository<Interested, Long> {
    List<Interested> findByMemberId(Long memberId);
}