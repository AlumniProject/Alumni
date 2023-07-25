package Alumni.backend.module.repository;

import Alumni.backend.module.domain.CommentLike;
import Alumni.backend.module.domain.PostLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
  @Query("select c from CommentLike c where c.member.id = :memberId and c.comment.id = :commentId")
  Optional<CommentLike> findByMemberAndComment(@Param("memberId") Long memberId, @Param("commentId") Long commentId);

  @Query("select c from CommentLike c where c.comment.id = :id")
  List<CommentLike> findByCommentId(@Param("id") Long id);
}
