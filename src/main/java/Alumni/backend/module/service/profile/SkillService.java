package Alumni.backend.module.service.profile;

import Alumni.backend.infra.exception.BadRequestException;
import Alumni.backend.module.domain.Profile.MySkill;
import Alumni.backend.module.domain.Profile.Skill;
import Alumni.backend.module.dto.contest.MateRequestDto;
import Alumni.backend.module.dto.contest.MateResponseDto;
import Alumni.backend.module.dto.profile.SkillDto;
import Alumni.backend.module.repository.profile.MySkillRepository;
import Alumni.backend.module.repository.profile.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final MySkillRepository mySkillRepository;

    @PostConstruct
    public void initInterestFieldData() throws IOException {
        if (skillRepository.count() == 0) { // 저장된 관심분야가 없으면 실행
            List<String> list = Arrays.asList("JAVA", "MySQL", "Spring Boot", "AWS", "JPA",
                    "Spring Security", "Python", "Flutter", "QueryDSL");
            List<Skill>skills = list.stream()
                    .map(s -> Skill.builder()
                            .skillName(s)
                            .build())
                    .collect(Collectors.toList());
            skillRepository.saveAll(skills);
        }
    }

    public List<SkillDto> findAllSkills() {
        return skillRepository.findAll().stream().map(SkillDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MateResponseDto> recommendMate(MateRequestDto mateRequestDto) {
        List<String> skills = getSkills(mateRequestDto.getField());
        List<Long> memberIds = mySkillRepository.recommendMemberByBackend(skills);
        List<MySkill> fetchJoinSkill = mySkillRepository.findByMemberIdInFetchJoinSkill(memberIds);

        List<MateResponseDto> mateResponseDtos = new ArrayList<>();
        for (Long memberId : memberIds) {
            MateResponseDto mateResponseDto = createSkillField(memberId, fetchJoinSkill);
            mateResponseDtos.add(mateResponseDto);
        }
        return mateResponseDtos;
    }

    private MateResponseDto createSkillField(Long memberId, List<MySkill> fetchJoinSkill) {
        MateResponseDto mateResponseDto = new MateResponseDto();
        List<String> skills = new ArrayList<>();
        boolean mateFlag = false;
        for (MySkill mySkill : fetchJoinSkill) {
            if (mySkill.getMember().getId().equals(memberId)) {
                if (!mateFlag) {
                    mateResponseDto.setMemberInfo(mySkill.getMember());
                    mateFlag = true;
                }
                skills.add(mySkill.getSkill().getSkillName());
            }
        }
        mateResponseDto.setSkills(skills);
        return mateResponseDto;
    }

    private List<String> getSkills(String field) {
        if (field.equals("Back-end")) {
            return List.of("JAVA", "MySQL", "Spring Boot", "AWS", "JPA", "Spring Security", "QueryDSL");
        } else if (field.equals("Front-end")) {
            return List.of("Flutter");
        } else if (field.equals("AI")) {
            return List.of("Python");
        } else { // TODO: 나머지 분야 추가
            throw new BadRequestException("INPUT_ERROR");
        }
    }
}