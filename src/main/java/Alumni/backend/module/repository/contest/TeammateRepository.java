package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Teammate;
import Alumni.backend.module.domain.registration.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface TeammateRepository extends JpaRepository<Teammate, Long>, TeammateRepositoryCustom {

    Optional<Teammate> findByMemberIdAndTeamId(Long memberId, Long teamId);

    List<Teammate> findByTeamIdAndMemberIn(Long teamId, List<Member> members);
}
