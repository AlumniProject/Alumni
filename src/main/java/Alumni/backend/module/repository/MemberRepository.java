package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);

  Boolean existsMemberByEmail(String email);

  Member findByRefreshToken(String refreshToken);
}