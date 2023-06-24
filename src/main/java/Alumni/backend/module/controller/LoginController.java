package Alumni.backend.module.controller;

import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.service.UniversityService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

  private final UniversityService universityService;

  @PostMapping("/member/email-validate")
  public ResponseEntity<? extends BasicResponse> emailValidate(
      @RequestBody Map<String, String> request) {
    return ResponseEntity.ok()
        .body(new SingleResponse(universityService.emailVerify(request.get("email"))));
  }
}
