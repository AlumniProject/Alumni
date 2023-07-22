package Alumni.backend.module.repository.Post;

import Alumni.backend.module.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByTitle(String title);

    @Modifying
    @Query("update Post p set p.commentNum = :commentNum where p.id = :id")
    int updateCommentCount(@Param("commentNum") Integer commentNum, @Param("id") Long id);
}
