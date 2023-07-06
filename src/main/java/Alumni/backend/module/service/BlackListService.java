package Alumni.backend.module.service;

import Alumni.backend.module.domain.BlackList;
import Alumni.backend.module.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;

    @Async
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void autoDelete() {
        // 현재 시간에서 1시간 뺀 값을 전달 -> 그 시간보다 createAt이 이전이면 삭제
        if (blackListRepository.count() != 0) {
            blackListRepository.deleteByCreateAtBefore(LocalDateTime.now().minusHours(1));
        }
    }

    public void saveBlackList(String accessToken) {
        BlackList blackList = BlackList.createBlackList(accessToken);
        blackListRepository.save(blackList);
    }
}
