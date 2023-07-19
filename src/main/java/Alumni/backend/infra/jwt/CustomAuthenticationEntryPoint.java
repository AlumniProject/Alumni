package Alumni.backend.infra.jwt;

import Alumni.backend.infra.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String exceptionMsg = (String) request.getAttribute(JwtProperties.EXCEPTION);
        response.setContentType("application/json;charset=UTF-8");
        if (exceptionMsg.equals("JWT_NOT_VALID") || exceptionMsg.equals("JWT_ACCESS_NOT_VALID")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(401, exceptionMsg)));
        } else if (exceptionMsg.equals("Internal Server Error")) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(500, exceptionMsg)));
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(exceptionMsg)));
        }
    }
}
