package Alumni.backend.module.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "프로필 게시물 DTD")
public class ProfilePostsResponseDto {
    @Schema(description = "사용자 프로필 정보")
    private ProfileResponseDto profileResponseDto;
    @Schema(description = "내 프로필인지")
    private boolean ownerStatus;
    @Schema(description = "내 프로필이 아닌경우 팔로우 하고 있는지 여부")
    private boolean followStatus;
    @Schema(description = "팔로워 수")
    private int follower;
    @Schema(description = "팔로잉 수")
    private int following;
    @Schema(description = "작성한 게시물 수")
    private int postCount;
    @Schema(description = "작성한 게시물 정보")
    private List<MyPostResponseDto> posts;

    @Builder
    public static ProfilePostsResponseDto getProfilePostsResponseDto(
            ProfileResponseDto profileResponseDto, boolean isOwner, boolean isFollow, int follower, int following, List<MyPostResponseDto> posts){
        return ProfilePostsResponseDto.builder()
                .profileResponseDto(profileResponseDto)
                .ownerStatus(isOwner)
                .followStatus(isFollow)
                .follower(follower)
                .following(following)
                .postCount(posts.size())
                .posts(posts)
                .build();
    }
}
