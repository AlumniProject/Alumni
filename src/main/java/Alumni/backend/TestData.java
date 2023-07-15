package Alumni.backend;

import Alumni.backend.infra.principal.PrincipalDetailsService;
import Alumni.backend.module.domain.*;
import Alumni.backend.module.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
@Transactional
@RequiredArgsConstructor
public class TestData {

    private final ImageRepository imageRepository;
    private final UniversityRepository universityRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final PrincipalDetailsService principalDetailsService;

    public void SetUp() {
        //given
        Image image1 = new Image("1", "1", "1");
        Image image2 = new Image("2", "2", "2");
        imageRepository.saveAll(Arrays.asList(image1, image2));

        University univ1 = universityRepository.findById(1L).get();
        University univ2 = universityRepository.findById(2L).get();

        Member member1 = Member.createMember("1", "1", "1", "1", univ1, "1");
        Member member2 = Member.createMember("2", "2", "2", "2", univ2, "2");
        Member member3 = Member.createMember("3", "3", "3", "3", univ1, "3");
        memberRepository.saveAll(Arrays.asList(member1, member2, member3));
        member1.uploadProfile(image1);
        member2.uploadProfile(image2);
        //member3.uploadProfile(image1);

        Board freeBoard = boardRepository.findByName("자유");
        Board techBoard = boardRepository.findByName("기술 Q&A");
        Board univBoard = boardRepository.findByName("우리 대학");
        Board compBoard = boardRepository.findByName("공모전");
        Post post1 = Post.createPost(member1, freeBoard, "t1", "c1");
        Post post2 = Post.createPost(member2, techBoard, "t2", "c2");
        Post post3 = Post.createPost(member3, univBoard, "t3", "c3");
        Post post4 = Post.createPost(member1, univBoard, "t4", "c4");
        Post post5 = Post.createPost(member2, compBoard, "t5", "c5");
        postRepository.saveAll(Arrays.asList(post1, post2, post3, post4, post5));

        Tag pythonTag = tagRepository.findByName("python").orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그 입니다."));
        Tag javaTag = tagRepository.findByName("Java").orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그 입니다."));

        PostTag postTag1 = PostTag.createPostTag(post2, pythonTag);
        PostTag postTag2 = PostTag.createPostTag(post2, javaTag);
        postTagRepository.saveAll(Arrays.asList(postTag1, postTag2));
    }

    public void SetUpOnlyMember() {
        //given
        Image image1 = new Image("1", "1", "1");
        imageRepository.save(image1);

        University univ1 = universityRepository.findById(1L).get();

        Member member1 = Member.createMember("1", "1", "1", "1", univ1, "1");
        memberRepository.save(member1);
        member1.uploadProfile(image1);
    }

    public Member findMemberByEmail(String email) {
        if (!memberRepository.existsMemberByEmail(email)) {
            University univ1 = universityRepository.findById(1L).get();
            Member member1 = Member.createMember(email, "1", "1", "1", univ1, "1");
            memberRepository.save(member1);
        }
        return memberRepository.findByEmail(email).get();
    }
}
