package Alumni.backend.module.repository.Contest;

import Alumni.backend.module.domain.Contest;

import java.util.List;
import java.util.Optional;

public interface ContestRepositoryCustom {
    Optional<List<Contest>> findByContent(String content);
}
