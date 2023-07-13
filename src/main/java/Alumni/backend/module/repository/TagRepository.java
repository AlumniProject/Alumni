package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findTop5ByOrderByCountDesc();

    Tag findByName(String name);
}
