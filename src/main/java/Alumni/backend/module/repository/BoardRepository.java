package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findById(Long id);
  
      Board findByName(String name);
}
