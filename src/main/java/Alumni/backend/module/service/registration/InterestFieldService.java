package Alumni.backend.module.service.registration;

import Alumni.backend.module.domain.registration.InterestField;
import Alumni.backend.module.repository.registration.InterestFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestFieldService {

    private final InterestFieldRepository interestFieldRepository;

    @PostConstruct
    public void initInterestFieldData() throws IOException {
        if (interestFieldRepository.count() == 0) { // 저장된 관심분야가 없으면 실행
            List<String> list = Arrays.asList("Frontend", "Backend", "모바일 앱", "게임 개발", "데이터 사이언스",
                    "AI", "블록체인", "디자인", "네트워크");
            List<InterestField> interestFields = list.stream()
                    .map(l -> InterestField.builder()
                            .fieldName(l)
                            .build())
                    .collect(Collectors.toList());
            interestFieldRepository.saveAll(interestFields);
        }
    }
}
