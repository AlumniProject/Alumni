package Alumni.backend.module.service.registration;

import Alumni.backend.module.domain.registration.Terms;
import Alumni.backend.module.repository.registration.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    @PostConstruct
    public void initTermsData() throws IOException {
        if (termsRepository.count() == 0) {
            ClassPathResource classPathResource1 = new ClassPathResource("terms_of_service.txt");
            ClassPathResource classPathResource2 = new ClassPathResource("privacy_policy.txt");
            saveTerm("서비스이용약관 동의 (필수)", classPathResource1);
            saveTerm("개인정보 수집 및 이용 동의 (필수)", classPathResource2);
        }
    }

    private void saveTerm(String title, ClassPathResource classPathResource) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream(),
                StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        Terms terms = Terms.createTerms(title, stringBuilder.toString());
        termsRepository.save(terms);
    }
}
