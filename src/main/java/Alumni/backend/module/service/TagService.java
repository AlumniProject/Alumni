package Alumni.backend.module.service;

import Alumni.backend.module.domain.Tag;
import Alumni.backend.module.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @PostConstruct
    public void initTagData() throws IOException {
        if (tagRepository.count() == 0) {
            List<String> list = Arrays.asList("python", "Java", "AI", "Front-End", "Back-End", "Kotlin");
            List<Tag> tags = list.stream()
                    .map(l -> Tag.builder()
                            .name(l)
                            .build())
                    .collect(Collectors.toList());
            tagRepository.saveAll(tags);
        }
    }
}
