package Alumni.backend.infra.jwt;

public interface JwtProperties {

  String SECRET = "alumniCommunity12349";
  int EXPIRATION_TIME = 864000000; // 10일
  String TOKEN_PREFIX = "Bearer ";
  String HEADER_STRING = "Authorization";
}
