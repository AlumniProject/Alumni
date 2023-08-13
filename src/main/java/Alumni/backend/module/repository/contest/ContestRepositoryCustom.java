package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Contest;

import java.util.List;
import java.util.Optional;

public interface ContestRepositoryCustom {
    Optional<List<Contest>> findByContent(String content);
}
