package Alumni.backend.infra.response;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "JWT_NOT_VALID",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "RUNTIME_ERROR",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
}
)
public @interface ApiDocumentAuthResponse {
}
