package Alumni.backend;

import Alumni.backend.module.domain.Post;
import Alumni.backend.module.domain.PostTag;
import Alumni.backend.module.domain.Tag;
import Alumni.backend.module.repository.ImageRepository;
import Alumni.backend.module.repository.MemberRepository;
import Alumni.backend.module.repository.PostTagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ImageTest {

    @Autowired private ImageRepository imageRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PostTagRepository postTagRepository;

}
