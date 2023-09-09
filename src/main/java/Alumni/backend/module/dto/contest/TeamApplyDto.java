package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Teammate;
import Alumni.backend.module.domain.registration.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "지원자 DTO")
public class TeamApplyDto {

    @Schema(description = "회원 ID", example = "1")
    private Long id; // 신청자 id
    @Schema(description = "대학명", example = "인하대학교")
    private String univ;
    @Schema(description = "전공명", example = "컴퓨터공학과")
    private String major;
    @Schema(description = "관심분야 리스트", example = "[\"AI\", \"Backend\"]")
    private List<String> interest;
    @Schema(description = "팀원 승인 여부", example = "true")
    private Boolean approve;
    @Schema(description = "닉네임", example = "kim")
    private String nickname;
    @Schema(description = "이미지 url", example = "http://image.~.jpg")
    private String imagePath;
    @Schema(description = "신청 날짜", example = "2023-07-06 07:45:31.997349", type = "String")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createTime;

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
                .createTime(teammate.getCreateTime())
                .build();
    }
}
