package Alumni.backend.module.controller;

import Alumni.backend.infra.jwt.JwtProperties;
import Alumni.backend.infra.jwt.JwtService;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.ErrorResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.service.UniversityService;
import java.util.Map;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.RefreshFailedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class LoginController {

  private final UniversityService universityService;
  private final JwtService jwtService;

  @PostMapping("/member/email-validate")
  public ResponseEntity<? extends BasicResponse> emailValidate(
      @RequestBody Map<String, String> request) {
    return ResponseEntity.ok()
        .body(new SingleResponse(universityService.emailVerify(request.get("email"))));
  }

  @PostMapping("/logout")
  public ResponseEntity<? extends BasicResponse> logout(HttpServletRequest request) {
    jwtService.logout(request);
    return ResponseEntity.ok().body(new SingleResponse("로그아웃 성공"));
  }

  @PostMapping("/reissue")
  public ResponseEntity<? extends BasicResponse> reissue(HttpServletRequest request,
                                                         HttpServletResponse response) {
    String result = jwtService.verifyToken(request, response);
    if (result.equals("-1")) {
      response.setStatus(401);
      return ResponseEntity.ok().body(new ErrorResponse(401, "refresh token 만료"));
    }
    return ResponseEntity.ok().body(new SingleResponse(result));
  }
}
