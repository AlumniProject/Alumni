package Alumni.backend.module.dto.community;

import Alumni.backend.module.domain.registration.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 프로필")
public class MemberProfileDto {
    @NotNull
    private Long memberId;
    @NotEmpty
    private String university;
    @NotEmpty
    private String major;

    private String profileImage;

    public static MemberProfileDto memberProfileDto(Member member){
        if(member.getProfileImage() != null){
            return MemberProfileDto.builder()
                    .memberId(member.getId())
                    .university(member.getUniversity().getName())
                    .major(member.getMajor())
                    .profileImage(member.getProfileImage().getImagePath())
                    .build();
        }
        else{
            return MemberProfileDto.builder()
                    .memberId(member.getId())
                    .university(member.getUniversity().getName())
                    .major(member.getMajor())
                    .build();
        }
    }
}
