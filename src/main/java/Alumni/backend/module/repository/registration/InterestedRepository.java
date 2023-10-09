package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.Interested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface InterestedRepository extends JpaRepository<Interested, Long>, InterestedRepositoryCustom{
    List<Interested> findByMemberId(Long memberId);
    @Query("select i.interestField.fieldName from Interested i where i.member.id = :memberId")
    Optional<List<String>> findInterestedById(@Param("memberId") Long memberId);

    @Transactional
    void deleteAllByMemberId(Long memberId);
}