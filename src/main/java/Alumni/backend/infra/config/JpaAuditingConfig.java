package Alumni.backend.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @WebMvc 테스트 시 jpa 생성과 관련된 빈들은 등록하지 않기 때문에 따로 빼서 등록
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
