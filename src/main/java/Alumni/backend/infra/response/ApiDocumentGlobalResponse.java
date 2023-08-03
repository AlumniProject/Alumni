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
                responseCode = "403",
                description = "USER_NOT_FOUND",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
}
)
public @interface ApiDocumentGlobalResponse {
}
