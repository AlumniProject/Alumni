package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.jwt.JwtService;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.ErrorResponse;
import Alumni.backend.infra.response.GeneralResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.service.UniversityService;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "Login", description = "로그인 관련 api")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UniversityService universityService;
    private final JwtService jwtService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증번호 발급 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "학교 이메일 형식이 아닙니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "이메일 검증", description = "이메일 검증 메서드 입니다.")
    @PostMapping("/member/email-validate")
    public ResponseEntity<? extends BasicResponse> emailValidate(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok()
                .body(new SingleResponse(universityService.emailVerify(request.get("email"))));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "로그아웃", description = "로그아웃 메서드 입니다.")
    @PostMapping("/logout")
    public ResponseEntity<? extends BasicResponse> logout(@CurrentUser Member member, HttpServletRequest request) {
        jwtService.logout(member, request);
        return ResponseEntity.ok().body(new SingleResponse("로그아웃 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ALL_TOKEN_VALID", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "200", description = "refreshToken 갱신 완료 accessToken 갱신 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "200", description = "accessToken 갱신 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "refreshToken 만료", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "재발급", description = "토큰 재발급 메서드 입니다.")
    @PostMapping("/reissue")
    public ResponseEntity<? extends BasicResponse> reissue(HttpServletRequest request,
                                                           HttpServletResponse response) {
        String result = jwtService.verifyToken(request, response);
        if (result.equals("-1")) {
            response.setStatus(401);
            return ResponseEntity.ok().body(new ErrorResponse(401, "refresh token 만료"));
        } else if (result.equals("")) {
            return ResponseEntity.ok().body(new SingleResponse("ALL_TOKEN_VALID"));
        }
        return ResponseEntity.ok().body(new SingleResponse(result));
    }
}
