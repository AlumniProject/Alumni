package Alumni.backend.module.repository.contest;

import Alumni.backend.module.domain.contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long>, ContestRepositoryCustom {
//    @Modifying
//    @Query("update Contest c set c.likeNum = :likeNum where c.id = :id")
//    int updateLikeCount(@Param("likeNum") Integer likeNum, @Param("id") Long id);//좋아요 수

}
