package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.profile.FriendDto;
import Alumni.backend.module.service.profile.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ApiDocumentGlobalResponse
@ApiDocumentAuthResponse
@Tag(name = "Friends", description = "친구 찾기 관련 api")
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 찾기 완료 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 회원" + "<br>관심분야 없음" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "친구 찾기", description = "관심분야가 비슷한 친구를 찾아주는 메서드")
    @GetMapping("/find/friends")
    public ResponseEntity<?extends BasicResponse> findFriends(@CurrentUser Member member){
        List<FriendDto> friends = friendService.findFriends(member);
        return ResponseEntity.ok().body(new DataResponse<>(friends, "친구 찾기 완료"));
    }
}
