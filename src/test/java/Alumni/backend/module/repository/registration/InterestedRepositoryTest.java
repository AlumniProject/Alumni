package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.InterestField;
import Alumni.backend.module.domain.registration.Interested;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Persistence Layer 역할
 * Data Access의 역할 -> 비즈니스 로직 침투 x
 * Data 대한 CRUD에만 집중한 Layer
 */

@SpringBootTest
@ActiveProfiles("test")
class InterestedRepositoryTest {
    @Autowired private InterestedRepository interestedRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private InterestFieldRepository interestFieldRepository;
    @Autowired private UniversityRepository universityRepository;

    @DisplayName("회원 아이디로 관심분야를 찾는다")
    @Test
    void findInterestedById(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember("soeun8636@yu.ac.kr", "닉네임", "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        InterestField interestField1 = interestFieldRepository.findById(1L).get();//Frontend
        InterestField interestField2 = interestFieldRepository.findById(2L).get();//Backend

        Interested interested1 = Interested.createInterested(saveMember, interestField1);
        Interested interested2 = Interested.createInterested(saveMember, interestField2);

        interestedRepository.saveAll(List.of(interested1, interested2));

        //when
        List<String> interestedList = interestedRepository.findInterestedById(saveMember.getId()).get();

        //then
        assertThat(interestedList).hasSize(2)
                .contains("Frontend", "Backend");
    }
}