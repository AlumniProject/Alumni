package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
    //Optional<List<Team>> findByContestId(Long contestId);

    List<Team> findByMemberId(Long memberId);
}
