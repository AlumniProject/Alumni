package Alumni.backend.module.controller;

import Alumni.backend.infra.response.*;
import Alumni.backend.module.dto.ContestResponseDto;
import Alumni.backend.module.service.ContestService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiDocumentGlobalResponse
@ApiDocumentAuthResponse
@Tag(name = "Contest", description = "공모전 관련 api")
@RestController
@RequestMapping("/contest")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "검색 결과 없음" + "<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "공모전 검색", description = "공모전 내용으로 검색하는 메서드 입니다.")
    @Parameter(name = "content", description = "검색 내용", example = "개발", in = ParameterIn.QUERY)
    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> viewContestDetail(@RequestParam(value = "content") String content) {

        List<ContestResponseDto> contestResponseDtos = contestService.contestSearch(content);

        return ResponseEntity.ok().body(new GeneralResponse<>(contestResponseDtos, "SUCCESS"));
    }
}
