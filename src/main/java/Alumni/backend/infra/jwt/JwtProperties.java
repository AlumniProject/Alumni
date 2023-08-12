package Alumni.backend.infra.jwt;

public interface JwtProperties {
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String HEADER_REFRESH = "Authorization-refresh";
    String EXCEPTION = "Exception";
}
