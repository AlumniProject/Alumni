package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Image;
import Alumni.backend.module.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Member findByRefreshToken(String refreshToken);

    Boolean existsMemberByEmail(String email);

    @Query("select m.profileImage from Member m where m.id = :id")
    Optional<Image> findImageByMemberId(@Param("id") Long id);

    Boolean existsMemberByNickname(String nickname);
}