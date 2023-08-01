package Alumni.backend.module.controller;

import Alumni.backend.infra.response.ApiDocumentResponse;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.GeneralResponse;
import Alumni.backend.module.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiDocumentResponse
@Tag(name = "Tag", description = "태그 관련 api")
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "해시태그 조회", description = "모든 해시태그 조회 메서드 입니다.")
    @ApiResponse(responseCode = "200", description = "모든 해시태그 조회 완료", content = @Content(schema = @Schema(implementation = GeneralResponse.class)))
    @GetMapping("/all")
    public ResponseEntity<? extends BasicResponse> findAllTag() {
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(tagService.findAllTagDto(), "모든 해시태그 조회 완료"));
    }
}
