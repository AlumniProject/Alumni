package Alumni.backend.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true); // 내 서버가 응답할 때 json을 처리할 수 있게 설정
        corsConfiguration.addAllowedOrigin("*"); // 모든 ip에 응답 허용
        corsConfiguration.addAllowedHeader("*"); // 모든 헤더에 응답 허용
        corsConfiguration.addAllowedMethod("*"); // 모든 http 메서드 요청 허용

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
