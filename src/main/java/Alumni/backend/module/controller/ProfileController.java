package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.service.Profile.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiDocumentGlobalResponse
@ApiDocumentAuthResponse
@Tag(name = "Profile", description = "사용자 관련 api")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final FollowService followService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로우 완료" + "<br>팔로우 취소 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팔로우 또는 팔로우 취소", description = "팔로우를 하거나 했던 팔로우를 취소하는 메서드입니다.")
    @Parameter(name = "member_id", description = "팔로우 또는 취소할 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/follow/{member_id}")
    public ResponseEntity<? extends BasicResponse> follow(@CurrentUser Member currentMember, @PathVariable("member_id") Long memberId){

        String message = followService.follow(currentMember, memberId);

        return ResponseEntity.ok().body(new SingleResponse(message));
    }
}
