package Alumni.backend.module.repository.Comment;

import Alumni.backend.module.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCutom {

    @Query("select c from Comment c where c.post.id = :id")
    List<Comment> findByPostId(@Param("id") Long id);
}