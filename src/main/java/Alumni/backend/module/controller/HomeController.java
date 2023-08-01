package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.dto.PopularPostResponseDto;
import Alumni.backend.module.service.HomeService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiDocumentResponse
@Tag(name = "Home", description = "home 관련 api")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

  private final HomeService homeService;

  @ApiResponse(responseCode = "200", description = "인기 게시글 조회 완료", content = @Content(schema = @Schema(implementation = GeneralResponse.class)))
  @Operation(summary = "인기글 조회", description = "상위 4개 인기글 조회 메서드 입니다.")
  @GetMapping
  public ResponseEntity<? extends BasicResponse> home(@CurrentUser Member member){

    List<PopularPostResponseDto> popularPosts = homeService.findPopularPosts(member);

    return ResponseEntity.ok()
        .body(new GeneralResponse<>(popularPosts, "인기 게시글 조회 완료"));
  }
}
