package Alumni.backend.module.service;

import Alumni.backend.module.domain.University;
import Alumni.backend.module.domain.VerifiedEmail;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.repository.UniversityRepository;
import Alumni.backend.module.repository.VerifiedEmailRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final EmailService emailService;
    private final VerifiedEmailRepository verifiedEmailRepository;

    @PostConstruct
    public void initUniversityData() throws IOException {
        if (universityRepository.count() == 0) { // 저장된 대학이메일 정보가 없을 때 실행
            //Resource resource = new ClassPathResource("university_email_data.csv");
            InputStream inputStream = new ClassPathResource("university_email_data.csv").getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            List<University> universityList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(","); // 쉽표를 기준으로 데이터 나누기
                if (split.length < 3) {
                    universityList.add(University.builder()
                            .name(split[1])
                            .build());
                } else {
                    universityList.add(University.builder()
                            .name(split[1])
                            .univEmail1(split[3])
                            .univEmail2(split[4])
                            .build());
                }
            }

            /*List<University> universityList = Files.readAllLines(somethingFile.toPath(), StandardCharsets.UTF_8)
                    .stream().map(
                            line -> {
                                String[] split = line.split(","); // 쉽표를 기준으로 데이터 나누기
                                if (split.length < 3) {
                                    return University.builder()
                                            .name(split[1])
                                            .build();
                                }
                                return University.builder()
                                        .name(split[1])
                                        .univEmail1(split[3])
                                        .univEmail2(split[4])
                                        .build();
                            }).collect(Collectors.toList());*/
            universityRepository.saveAll(universityList);
        }
    }

    public String emailVerify(String email) {
        //학교 이메일인지 확인
        //인증번호 발급

        /**
         * 정규식으로 바꿔서 형식 구별하기
         */
        int index = email.indexOf("@");
        String univEmail = email.substring(index);//@뒷부분

        if (univEmail.length() < 5) {
            throw new IllegalArgumentException("Bad Request");
        }

        //이메일 검증
        if (!universityRepository.existsByUnivEmail1(univEmail)) {
            if (!universityRepository.existsByUnivEmail2(univEmail)) {
                throw new NoExistsException("학교 이메일 형식이 아닙니다.");
            }
        }

        // 유요한 학교이메일 형식이면 인증번호 생성하여 메일 전송
        VerifiedEmail verifiedEmail;
        if (verifiedEmailRepository.existsByEmail(email)) {
            verifiedEmail = verifiedEmailRepository.findByEmail(email).get();
            verifiedEmail.verifiedFalse();
            verifiedEmail.generateEmailToken();
        } else {
            verifiedEmail = VerifiedEmail.createVerifiedEmail(email);
            verifiedEmailRepository.save(verifiedEmail); //이메일, 인증번호 verifiedEmail 테이블에 저장
        }

        emailService.sendMail(verifiedEmail.getEmail(), verifiedEmail.getEmailCode());

        return "인증번호 발급 완료";
    }
}