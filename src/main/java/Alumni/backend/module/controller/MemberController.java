package Alumni.backend.module.controller;

import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.GeneralResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.BackendApplication;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Terms;
import Alumni.backend.module.dto.*;
import Alumni.backend.module.dto.requestDto.LoginRequestDto;
import Alumni.backend.module.dto.requestDto.SignUpRequestDto;
import Alumni.backend.module.dto.requestDto.interestFieldRequestDto;
import Alumni.backend.module.service.ImageService;
import Alumni.backend.module.service.MemberService;
import Alumni.backend.module.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ImageService imageService;

    @PostMapping("/login")
    public ResponseEntity<? extends BasicResponse> login(@RequestBody @Valid LoginRequestDto request){

        String email = request.getEmail();
        String emailCode = request.getCertification();
        String fcmToken = request.getFcmToken();

        return ResponseEntity.ok()
                .body(new SingleResponse(memberService.login(email, emailCode, fcmToken)));
    }

    @PostMapping("/member/sign-up")
    public ResponseEntity<? extends BasicResponse> signUp(@RequestBody @Valid SignUpRequestDto request){

        memberService.signUp(request);

        return ResponseEntity.ok().body(new SingleResponse("회원가입 완료"));
    }

    @PostMapping("/member/inquiry")
    public ResponseEntity<? extends BasicResponse> inquiry(@RequestBody @Valid Map<String,String> request){

        memberService.SaveInquiry(request.get("content"));

        return ResponseEntity.ok().body(new SingleResponse("문의 완료"));
    }

    @GetMapping("/member/terms")
    public ResponseEntity<? extends BasicResponse> terms(){
        List<Terms> terms = memberService.findTerms();

        List<TermsDto> termsDto = terms.stream()
                .map(t -> new TermsDto(t.getId(), t.getTitle(), t.getContent()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new GeneralResponse<>(termsDto, "약관내용 전송 완료"));
    }

    @PutMapping("/member/interest-field")
    public ResponseEntity<? extends BasicResponse> interest(@RequestBody interestFieldRequestDto data){
        /**
         * jwt 토큰에서 memberId 가져오기
         */
        //memberService.updateInterest(data.getData(), memberId);//data, memberId

        return ResponseEntity.ok().body(new SingleResponse("관심분야 추가 완료"));
    }

    @PostMapping("/member/profile-image")
    public ResponseEntity<? extends BasicResponse> profileImage(@RequestPart("file") MultipartFile multipartFile){

        String storageImageName = imageService.saveProfileImage(multipartFile);//s3에 저장
         /**
         * jwt 토큰에서 memberId 가져오기
         */
        //memberService.uploadProfileImage(memberId, storageImageName);//회원 프로필 update(member table 에 저장)
        return ResponseEntity.ok().body(new SingleResponse("이미지 저장 완료"));
    }
}