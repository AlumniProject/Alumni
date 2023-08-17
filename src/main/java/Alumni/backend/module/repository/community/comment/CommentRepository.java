package Alumni.backend.module.repository.community.comment;

import Alumni.backend.module.domain.community.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    List<Comment> findByTeamId(Long teamId);

    @Query("select c from Comment c where c.post.id = :id")
    List<Comment> findByPostId(@Param("id") Long id);

    @Modifying
    @Query("update Comment c set c.likeNum = :likeNum where c.id = :id")
    int updateLikeCount(@Param("likeNum") Integer likeNum, @Param("id") Long id);//좋아요 수

    @Transactional
    void deleteAllByMemberId(Long memberId);
}
