package Alumni.backend.infra.jwt;

public interface JwtProperties {

    String SECRET = "alumniCommunity12349";
    int EXPIRATION_TIME = 3600000; // 1시간
    int REFRESH_EXPIRATION_TIME = 1209600000; // 14일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String HEADER_REFRESH = "Authorization-refresh";
    String EXCEPTION = "Exception";
}
