package Alumni.backend.module.dto.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "홈 화면")
public class HomeDto {
    private MemberProfileDto profile;
    private List<PopularPostResponseDto> popularPosts;

    public void setHomeDto(MemberProfileDto profile, List<PopularPostResponseDto> popularPosts){
        this.profile = profile;
        this.popularPosts = popularPosts;
    }
}
