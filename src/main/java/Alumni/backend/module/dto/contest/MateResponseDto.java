package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.registration.MemberResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class MateResponseDto {

    private MemberResponseDto member;
    private String univName;
    private String majorName;
    private List<String> skills;

    public void setMemberInfo(Member member) {
        this.member = MemberResponseDto.getMemberResponseDto(member);
        this.univName = member.getUniversity().getName();
        this.majorName = member.getMajor();
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
