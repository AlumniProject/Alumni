package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.post.id = :id")
    List<Comment> findByPostId(@Param("id") Long id);
}
