package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.registration.Member;
import lombok.Data;

import java.util.List;

@Data
public class MateResponseDto {

    private Long memberId;
    private String univName;
    private String majorName;
    private List<String> skills;

    public void setMemberInfo(Member member) {
        this.memberId = member.getId();
        this.univName = member.getUniversity().getName();
        this.majorName = member.getMajor();
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
