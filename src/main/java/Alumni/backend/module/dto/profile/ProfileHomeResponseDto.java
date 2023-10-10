package Alumni.backend.module.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "프로필 게시물 DTD")
public class ProfileHomeResponseDto {
    @Schema(description = "사용자 프로필 정보")
    private ProfileResponseDto profile;
    @Schema(description = "내 프로필인지")
    private boolean ownerStatus;
    @Schema(description = "내 프로필이 아닌경우 팔로우 하고 있는지 여부")
    private boolean followStatus;
    @Schema(description = "팔로워 수")
    private int follower;
    @Schema(description = "팔로잉 수")
    private int following;
    @Schema(name = "interestedFieldList", description = "관심분야", example = "[\"Flutter\", \"JPA\"]")
    private List<String> interestedFieldList;
    @Schema(name = "skill", description = "스킬", example = "[\"Flutter\", \"JPA\"]")
    private List<String> skillList;
    @Schema(description = "인스타그램 링크")
    private String instagram;
    @Schema(description = "깃허브 링크")
    private String github;
    @Schema(description = "페이스북 링크")
    private String facebook;

    @Builder
    public static ProfileHomeResponseDto getProfileHomeResponse(ProfileResponseDto profile, Boolean isOwner, Boolean isFollow, int follower, int following,
                                                                List<String> interestedFieldList, List<String> skillList,
                                                                String instagram, String github, String facebook){
        return ProfileHomeResponseDto.builder()
                .profile(profile)
                .ownerStatus(isOwner)
                .followStatus(isFollow)
                .follower(follower)
                .following(following)
                .interestedFieldList(interestedFieldList)
                .skillList(skillList)
                .instagram(instagram == null ? "" : instagram)
                .github(github == null ? "" : github)
                .facebook(facebook == null ? "" : facebook)
                .build();
    }
}