package Alumni.backend;

import Alumni.backend.module.repository.ImageRepository;
import Alumni.backend.module.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ImageTest {

    @Autowired private ImageRepository imageRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    public void 이미지_업로드_테스트(){
    }
}
