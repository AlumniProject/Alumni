package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Terms;
import Alumni.backend.module.dto.*;
import Alumni.backend.module.dto.requestDto.InquiryRequestDto;
import Alumni.backend.module.dto.requestDto.SignUpRequestDto;
import Alumni.backend.module.dto.requestDto.interestFieldRequestDto;
import Alumni.backend.module.service.ImageService;
import Alumni.backend.module.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@ApiDocumentGlobalResponse
@Tag(name = "Member", description = "회원 관련 api")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ImageService imageService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "검증되지 않은 이메일입니다" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "이미 존재하는 nickname입니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "회원가입", description = "회원가입 메서드 입니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<? extends BasicResponse> signUp(@RequestBody @Valid SignUpRequestDto request,
                                                          HttpServletResponse response) {
        memberService.signUp(request, response);
        return ResponseEntity.ok().body(new SingleResponse("회원가입 완료"));
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문의 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "문의하기", description = "문의하기 메서드 입니다.")
    @PostMapping("/inquiry")
    public ResponseEntity<? extends BasicResponse> inquiry(@RequestBody @Valid InquiryRequestDto inquiryRequestDto) {

        memberService.SaveInquiry(inquiryRequestDto.getContent());

        return ResponseEntity.ok().body(new SingleResponse("문의 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약관내용 전송 완료", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "약관동의", description = "약관동의 메서드 입니다.")
    @GetMapping("/terms")
    public ResponseEntity<? extends BasicResponse> terms() {
        List<Terms> terms = memberService.findTerms();

        List<TermsDto> termsDto = terms.stream()
                .map(t -> new TermsDto(t.getId(), t.getTitle(), t.getContent()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new GeneralResponse<>(termsDto, "약관내용 전송 완료"));
    }

    @ApiDocumentAuthResponse
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심분야 추가 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "field가 존재하지 않습니다" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "관심분야 설정", description = "관심분야 설정 메서드 입니다.")
    @PutMapping("/interest-field")
    public ResponseEntity<? extends BasicResponse> interest(@Schema(hidden = true) @CurrentUser Member member, @RequestBody interestFieldRequestDto data) {
        memberService.updateInterest(data.getData(), member.getId());//data, memberId

        return ResponseEntity.ok().body(new SingleResponse("관심분야 추가 완료"));
    }

    @ApiDocumentAuthResponse
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 저장 완료", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
            @ApiResponse(responseCode = "413", description = "이미지 용량이 큽니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "프로필 설정", description = "프로필 설정 메서드 입니다.")
    @PostMapping("/profile-image")
    public ResponseEntity<? extends BasicResponse> profileImage(@Schema(hidden = true) @CurrentUser Member member,
                                                                @RequestPart("image") MultipartFile multipartFile) {
        String storageImageName = imageService.saveProfileImage(multipartFile);//s3에 저장
        memberService.uploadProfileImage(member.getId(), storageImageName);//회원 프로필 update(member table 에 저장)

        return ResponseEntity.ok().body(new SingleResponse("이미지 저장 완료"));
    }

    @ApiDocumentAuthResponse
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 메서드 입니다.")
    @DeleteMapping("/edit/delete")
    public ResponseEntity<? extends BasicResponse> deleteMember(@Schema(hidden = true) @CurrentUser Member member) {
        memberService.deleteMember(member);
        return ResponseEntity.ok().body(new SingleResponse("회원 탈퇴 완료"));
    }
}