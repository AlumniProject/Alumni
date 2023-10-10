package Alumni.backend.module.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FriendDto {
    @Schema(description = "사용자 프로필 정보")
    private ProfileResponseDto profile;
    @Schema(description = "팔로우 여부")
    private Boolean followStatus;
    @Schema(name = "interestedFieldList", description = "관심분야", example = "[\"Flutter\", \"JPA\"]")
    private List<String> interestedFieldList;

    @Builder
    public static FriendDto getFriendDto(ProfileResponseDto profile, Boolean followStatus, List<String> interestedFieldList){
        return FriendDto.builder()
                .profile(profile)
                .followStatus(followStatus)
                .interestedFieldList(interestedFieldList)
                .build();
    }
}
