package Alumni.backend.module.controller;

import Alumni.backend.BackendApplication;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Terms;
import Alumni.backend.module.dto.ListResponse;
import Alumni.backend.module.dto.LoginRequestDto;
import Alumni.backend.module.dto.SignUpRequestDto;
import Alumni.backend.module.dto.TermsDto;
import Alumni.backend.module.service.ImageService;
import Alumni.backend.module.service.MemberService;
import Alumni.backend.module.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ImageService imageService;

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

        /**
         * jwt 토큰에서 memberId 가져오기
         */
        //memberService.updateInterest();//data, memberId

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message", "문의 완료");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/profile-image")
    public ResponseEntity<Map<String, Object>> profileImage(@RequestPart("file") MultipartFile multipartFile){

        String storageImageName = imageService.saveProfileImage(multipartFile);//s3에 저장
         /**
         * jwt 토큰에서 memberId 가져오기
         */
        //memberService.uploadProfileImage(memberId, storageImageName);//회원 프로필 update(member table 에 저장)

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.OK.value());
        result.put("message", "이미지 저장 완료");

        return ResponseEntity.ok(result);
    }
}