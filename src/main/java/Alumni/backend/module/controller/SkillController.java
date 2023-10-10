package Alumni.backend.module.controller;

import Alumni.backend.infra.response.*;
import Alumni.backend.module.service.profile.SkillService;
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

@ApiDocumentAuthResponse
@ApiDocumentGlobalResponse
@Tag(name = "SKill", description = "스킬 관련 api")
@RestController
@RequestMapping("/skill")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 스킬 조회 완료", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "스킬 조회", description = "모든 스킬 조회 메서드 입니다.")
    @GetMapping("/all")
    public ResponseEntity<?extends BasicResponse> findAllSkills(){
        return ResponseEntity.ok().body(new GeneralResponse<>(skillService.findAllSkills(), "모든 기술 조회 완료"));
    }
}
