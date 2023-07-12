package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
}
