package Alumni.backend.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi alumniApi(){
        return GroupedOpenApi.builder()
                .group("Alumni-api")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI alumniOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Alumni API")
                        .description("동문개발자 프로젝트 API 명세서 입니다."));
    }

}