package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Comment;
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
    int updateCommentCount(@Param("commentNum") Integer commentNum, @Param("id") Long id);//댓글 수

    @Modifying
    @Query("update Post p set p.likeNum = :likeNum where p.id = :id")
    int updateLikeCount(@Param("likeNum") Integer likeNum, @Param("id") Long id);//좋아요 수
}
