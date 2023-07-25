package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.post.id = :id")
    List<Comment> findByPostId(@Param("id") Long id);

    @Modifying
    @Query("update Comment c set c.likeNum = :likeNum where c.id = :id")
    int updateLikeCount(@Param("likeNum") Integer likeNum, @Param("id") Long id);//좋아요 수
}
