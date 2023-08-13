package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.Interested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface InterestedRepository extends JpaRepository<Interested, Long> {
    List<Interested> findByMemberId(Long memberId);

    @Transactional
    void deleteAllByMemberId(Long memberId);
}