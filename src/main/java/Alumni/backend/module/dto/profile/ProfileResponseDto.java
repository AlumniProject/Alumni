package Alumni.backend.module.dto.profile;

import Alumni.backend.module.domain.registration.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "프로필 DTO")
public class ProfileResponseDto {
    private Long memberId;
    private String university;
    private String major;
    private String profileImage;
    private String classOf;
    private String nickname;
    private String introduction;
    private Boolean isOwner;
    private Boolean isFollow;

    @Builder
    public static ProfileResponseDto getProfileResponseDto(Member member, Boolean isOwner, Boolean isFollow) {
        return ProfileResponseDto.builder()
                .memberId(member.getId())
                .university(member.getUniversity().getName())
                .major(member.getMajor())
                .profileImage(member.getProfileImage() == null ? null : member.getProfileImage().getImagePath())
                .classOf(member.getClassOf())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction() == null ? "" : member.getIntroduction())
                .isOwner(isOwner)
                .isFollow(isFollow)
                .build();
    }
}
