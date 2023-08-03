package Alumni.backend.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

        //전역 인증 설정(JWT)
        String jwtSchemeName = "jwtAuth";//SecuritySchema명
        //API 요청 헤더에 인증 정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        //SecurityScheme 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)//HTTP 방식
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(new Info().title("Alumni API")
                        .description("동문개발자 프로젝트 API 명세서 입니다."))
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}