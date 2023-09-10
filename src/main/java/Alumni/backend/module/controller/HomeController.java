package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.MemberProfileDto;
import Alumni.backend.module.dto.community.PopularPostResponseDto;
import Alumni.backend.module.dto.community.SimpleContestDto;
import Alumni.backend.module.service.community.HomeService;
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

import java.util.List;

@ApiDocumentGlobalResponse
@ApiDocumentAuthResponse
@Tag(name = "Home", description = "home 관련 api")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

  private final HomeService homeService;

  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "홈 조회 완료", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
          @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                  + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                  content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Operation(summary = "인기글 조회&공모전 조회", description = "상위 4개 인기글 조회 & 촤신 공모전 5개 조회하는 메서드입니다. ")
  @GetMapping
  public ResponseEntity<? extends BasicResponse> home(@Schema(hidden = true) @CurrentUser Member member){

    List<PopularPostResponseDto> popularPosts = homeService.findPopularPosts(member);
    List< SimpleContestDto> simpleContestDtos = homeService.findTop5Contest();
    MemberProfileDto memberProfileDto =  homeService.findMemberProfile(member);

    return ResponseEntity.ok().body(new HomeResponse<>(memberProfileDto, simpleContestDtos, popularPosts));
  }
}
