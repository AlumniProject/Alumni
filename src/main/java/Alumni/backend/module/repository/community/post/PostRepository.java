package Alumni.backend.module.repository.community.post;

import Alumni.backend.module.domain.community.Post;
import java.util.List;
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
  
    @Query("select p.id from Post p where p.member.university.id = :id")
    List<Long> findByUniversityId(@Param("id") Long id);
  
    List<Post> findAllByMemberId(Long memberId);
}
