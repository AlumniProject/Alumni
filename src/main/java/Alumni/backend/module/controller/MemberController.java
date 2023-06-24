package Alumni.backend.module.controller;

import Alumni.backend.module.domain.Terms;
import Alumni.backend.module.dto.ListResponse;
import Alumni.backend.module.dto.LoginRequestDto;
import Alumni.backend.module.dto.SignUpRequestDto;
import Alumni.backend.module.dto.TermsDto;
import Alumni.backend.module.service.MemberService;
import Alumni.backend.module.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final UniversityService universityService;
    private final MemberService memberService;

    /*@PostMapping("/member/email-validate")
    public ResponseEntity<Map<String, Object>> emailValidate(@RequestBody Map<String, String> request){

        universityService.emailVerify(request.get("email"));

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message", "인증번호 발급 완료");

        return ResponseEntity.ok(result);
    }*/

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestDto request){

        String email = request.getEmail();
        String emailCode = request.getCertification();
        String fcmToken = request.getFcmToken();

        String message = memberService.login(email, emailCode, fcmToken);

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message", message);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/sign-up")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody @Valid SignUpRequestDto request){

        memberService.signUp(request);

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message", "회원가입 완료");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/inquiry")
    public ResponseEntity<Map<String, Object>> inquiry(@RequestBody @Valid Map<String,String> request){

        memberService.SaveInquiry(request.get("content"));

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message", "문의 완료");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/member/terms")
    public ListResponse terms(){
        List<Terms> terms = memberService.findTerms();

        List<TermsDto> termsDto = terms.stream()
                .map(t -> new TermsDto(t.getId(), t.getTitle(), t.getContent()))
                .collect(Collectors.toList());

        return new ListResponse(termsDto.size(), HttpStatus.OK.value(),
                "약관내용 전송 완료", termsDto);
    }

    @PutMapping("/member/interest-field")
    public ResponseEntity<Map<String, Object>> interest(@RequestBody List<String> data){

//        memberService.updateInterest();//data, userId

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message", "문의 완료");

        return ResponseEntity.ok(result);
    }
}