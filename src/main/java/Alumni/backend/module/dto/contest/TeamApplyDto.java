package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Teammate;
import Alumni.backend.module.domain.registration.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamApplyDto {

    private Long id; // 신청자 id
    private String univ;
    private String major;
    private List<String> interest;
    private Boolean approve;
    private String nickname;
    private String imagePath;

    public static TeamApplyDto getTeamApplyDto(Teammate teammate, List<String> fieldNames) {
        Member member = teammate.getMember();
        return TeamApplyDto.builder()
                .id(member.getId())
                .univ(member.getUniversity().getName())
                .major(member.getMajor())
                .interest(fieldNames)
                .approve(teammate.getApprove())
                .nickname(member.getNickname())
                .imagePath(member.getProfileImage() == null ? null : member.getProfileImage().getImagePath())
                .build();
    }
}
