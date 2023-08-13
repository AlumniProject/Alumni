package Alumni.backend.module.repository.community;

import Alumni.backend.module.domain.community.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findByName(String name);
    Optional<Board> findById(Long id);
}
