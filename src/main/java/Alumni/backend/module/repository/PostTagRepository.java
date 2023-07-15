package Alumni.backend.module.repository;

import Alumni.backend.module.domain.PostTag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @EntityGraph(attributePaths = {"tag"})
    List<PostTag> findByPostId(Long postId);
}
