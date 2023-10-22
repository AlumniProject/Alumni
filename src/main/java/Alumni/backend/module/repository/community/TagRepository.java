package Alumni.backend.module.repository.community;

import Alumni.backend.module.domain.community.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findTop5ByOrderByCountDesc();

    Optional<Tag> findByName(String name);
}
