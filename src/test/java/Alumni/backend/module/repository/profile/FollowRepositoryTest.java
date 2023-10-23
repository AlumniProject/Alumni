package Alumni.backend.module.repository.profile;

import Alumni.backend.module.domain.Profile.Follow;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FollowRepositoryTest {
    @Autowired private FollowRepository followRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        followRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("팔로워와 팔로잉 아이디로 follow를 찾는다")
    @Test
    void findByFollowerIdAndFollowingId(){
        //given
        Member member1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member member2 = createMember("test2@yu.ac.kr", "닉네임2");

        Follow follow = Follow.createFollow(member1, member2);
        followRepository.save(follow);

        //when
        Follow findFollow = followRepository.findByFollowerIdAndFollowingId(member1.getId(), member2.getId()).get();

        //then
        assertThat(findFollow).isNotNull();
        assertThat(findFollow.getFollower().getId()).isEqualTo(member1.getId());
        assertThat(findFollow.getFollowing().getId()).isEqualTo(member2.getId());
    }

    @DisplayName("팔로워 아이디로 팔로우 하고 있는 모든 아이디를 찾는다")
    @Test
    void findByFollowerId(){
        //given
        Member member1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member member2 = createMember("test2@yu.ac.kr", "닉네임2");
        Member member3 = createMember("test3@yu.ac.kr", "닉네임3");

        Follow follow1 = Follow.createFollow(member1, member2);
        Follow follow2 = Follow.createFollow(member1, member3);

       followRepository.saveAll(List.of(follow1, follow2));

        //when
        List<Long> followerIdList = followRepository.findByFollowerId(member1.getId());

        //then
        assertThat(followerIdList).hasSize(2)
                .contains(member2.getId(), member3.getId());
    }

    @DisplayName("팔로워 수 찾기")
    @Test
    void countByFollowerId(){
        //given
        Member member1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member member2 = createMember("test2@yu.ac.kr", "닉네임2");
        Member member3 = createMember("test3@yu.ac.kr", "닉네임3");

        Follow follow1 = Follow.createFollow(member1, member2);
        Follow follow2 = Follow.createFollow(member1, member3);

        followRepository.saveAll(List.of(follow1, follow2));

        //when
        int count = followRepository.countByFollowerId(member1.getId());

        //then
        assertThat(count).isEqualTo(2);

    }

    @DisplayName("팔로잉 수 찾기")
    @Test
    void countByFollowingI(){
        //given
        Member member1 = createMember("test1@yu.ac.kr", "닉네임1");
        Member member2 = createMember("test2@yu.ac.kr", "닉네임2");
        Member member3 = createMember("test3@yu.ac.kr", "닉네임3");

        Follow follow1 = Follow.createFollow(member1, member2);
        Follow follow2 = Follow.createFollow(member1, member3);

        followRepository.saveAll(List.of(follow1, follow2));

        //when
        int count = followRepository.countByFollowingId(member2.getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    private Member createMember(String email, String nickname) {
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember(email, nickname, "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        return saveMember;
    }
}