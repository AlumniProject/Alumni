package Alumni.backend.module.service;

import Alumni.backend.module.domain.Terms;
import Alumni.backend.module.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    @PostConstruct
    public void initTermsData() throws IOException {
        if (termsRepository.count() == 0) {
            Terms terms1 = Terms.createTerms("서비스이용약관 동의 (필수)",
                    "제1조(목적)\n동문개발자 서비스 이용약관은 ~");
            Terms terms2 = Terms.createTerms("개인정보 수집 및 이용 동의 (필수)",
                    "제1조(목적)\n동문개발자 서비스 이용약관은 ~");
            termsRepository.save(terms1);
            termsRepository.save(terms2);
        }
    }
}
