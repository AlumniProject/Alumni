package Alumni.backend.module.repository.community;

import Alumni.backend.module.domain.community.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TagRepositoryTest {

    @Autowired private TagRepository tagRepository;

    @DisplayName("많이 사용한 태그 상위 5개를 조회한다.")
    @Test
    public void 인기태그_조회_테스트() throws Exception {
        List<Tag> tags = tagRepository.findAll();

        for (int i = 0; i < tags.size(); i++) {
            tags.get(i).setCount(i);
        }

        List<Tag> tagList = tagRepository.findTop5ByOrderByCountDesc();
        assertThat(tagList).hasSize(5)
                .extracting("name")
                .contains("HTML", "CSS", "Javascript", "Node.js", "Vue.js");
    }

}