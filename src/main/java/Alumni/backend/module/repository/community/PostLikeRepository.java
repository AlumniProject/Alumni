package Alumni.backend.module.repository.community;

import Alumni.backend.module.domain.community.PostLike;
import java.util.List;
import java.util.Optional;

import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> , PostLikeRepositoryCustom{

  @Query("select p from PostLike p where p.member.id = :memberId and p.post.id = :postId")
  Optional<PostLike> findByMemberAndPost(@Param("memberId") Long memberId, @Param("postId") Long postId);

  @Query("select p from PostLike p where p.post.id = :id")
  List<PostLike> findByPostId(@Param("id") Long id);

  long countByPostId(Long postId);
}
