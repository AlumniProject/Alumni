package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.GeneralResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.dto.PopularPostResponseDto;
import Alumni.backend.module.service.HomeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

  private final HomeService homeService;

  @GetMapping
  public ResponseEntity<? extends BasicResponse> home(@CurrentUser Member member){

    List<PopularPostResponseDto> popularPosts = homeService.findPopularPosts(member);

    return ResponseEntity.ok()
        .body(new GeneralResponse<>(popularPosts, "인기 게시글 조회 완료"));
  }
}
