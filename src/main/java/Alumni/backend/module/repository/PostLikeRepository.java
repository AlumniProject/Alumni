package Alumni.backend.module.repository;

import Alumni.backend.module.domain.PostLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  @Query("select p from PostLike p where p.member.id = :memberId and p.post.id = :postId")
  Optional<PostLike> findByMemberAndPost(@Param("memberId") Long memberId, @Param("postId") Long postId);

  @Query("select p from PostLike p where p.post.id = :id")
  List<PostLike> findByPostId(@Param("id") Long id);
}
