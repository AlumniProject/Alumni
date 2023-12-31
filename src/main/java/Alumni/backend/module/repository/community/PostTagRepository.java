package Alumni.backend.module.repository.community;

import Alumni.backend.module.domain.community.PostTag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @EntityGraph(attributePaths = {"tag"})
    List<PostTag> findByPostId(Long postId);
}
