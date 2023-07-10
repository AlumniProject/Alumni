package Alumni.backend.module.repository;

import Alumni.backend.module.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    void deleteByCreateAtBefore(LocalDateTime localDateTime);

    Boolean existsBlackListByAccessToken(String accessToken);
}
