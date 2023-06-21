package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsMemberByEmail(String email);
}