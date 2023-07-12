package Alumni.backend.module.service;

import Alumni.backend.module.domain.*;
import Alumni.backend.module.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;

    @Test
    public void 인기태그_조회_테스트() throws Exception {
        List<Tag> tags = tagRepository.findAll();
        for (int i = 0; i < tags.size(); i++) {
            tags.get(i).setCount(i);
        }

        List<Tag> tagList = tagRepository.findTop5ByOrderByCountDesc();
        Assertions.assertEquals(5, tagList.size());
        Assertions.assertEquals("Kotlin", tagList.get(0).getName());
    }

    @Test
    public void 전체_게시글_조회_테스트() throws Exception {
        //given
        Image image1 = new Image("1", "1", "1");
        Image image2 = new Image("2", "2", "2");
        Image image3 = new Image("3", "3", "3");

        Optional<University> univ1 = universityRepository.findById(1L);
        Optional<University> univ2 = universityRepository.findById(2L);
        Optional<University> univ3 = universityRepository.findById(3L);

        Member member1 = Member.createMember("1", "1", "1", "1", univ1.get(), "1");
        Member member2 = Member.createMember("2", "2", "2", "2", univ1.get(), "2");
        Member member3 = Member.createMember("3", "3", "3", "3", univ2.get(), "3");
        Member member4 = Member.createMember("4", "4", "4", "4", univ2.get(), "4");
        Member member5 = Member.createMember("5", "5", "5", "5", univ3.get(), "5");
        memberRepository.saveAll(Arrays.asList(member1, member2, member3, member4, member5));
        member1.uploadProfile(image1);
        member2.uploadProfile(image2);
        member3.uploadProfile(image3);

        List<Board> boardList = boardRepository.findAll();




        //when

        //then
    }
}