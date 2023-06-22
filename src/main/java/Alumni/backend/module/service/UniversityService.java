package Alumni.backend.module.service;

import Alumni.backend.module.domain.VerifiedEmail;
import Alumni.backend.module.exception.NoExistsException;
import Alumni.backend.module.repository.UniversityRepository;
import Alumni.backend.module.repository.VerifiedEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UniversityRepository universityRepository;
    private final EmailService emailService;
    private final VerifiedEmailRepository verifiedEmailRepository;

    public void emailVerify(String email){
        //학교 이메일인지 확인
        //인증번호 발급

        /**
         * 정규식으로 바꿔서 형식 구별하기
         */
        int index = email.indexOf("@");
        String univEmail = email.substring(index+1);//@뒷부분

        if(univEmail.length()<5){
            throw new IllegalArgumentException("Bad Request");
        }

        //이메일 검증
        if(!universityRepository.existsByUnivEmail(univEmail)){//학교 이메일 형식이 아닌경우
            throw new NoExistsException("학교 이메일 형식이 아닙니다.");
        }

        String token = emailService.sendMail(email);//인증번호 발급
        VerifiedEmail verifiedEmail = VerifiedEmail.createVerifiedEmail(email, token);
        verifiedEmailRepository.save(verifiedEmail); //이메일, 인증번호 verifiedEmail 테이블에 저장

    }
}