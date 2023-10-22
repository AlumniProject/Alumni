package Alumni.backend.module.repository.community.post;

import Alumni.backend.module.domain.community.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByTitle(String title);
  
    @Query("select p.id from Post p where p.member.university.id = :id")
    List<Long> findByUniversityId(@Param("id") Long id);
  
    List<Post> findAllByMemberId(Long memberId);
}
