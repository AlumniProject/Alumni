package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@Schema(description = "회원 정보 DTO")
public class MemberResponseDto {

    @Schema(description = "작성자 정보", example = "1")
    private Long id;
    @Schema(description = "닉네임", example = "kim")
    private String nickname;
    @Schema(description = "이미지url", example = "~.jpg")
    private String imagePath;

    public static MemberResponseDto getMemberResponseDto(Member member) {
        if (member.getProfileImage() != null) {
            return MemberResponseDto.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .imagePath(member.getProfileImage().getImagePath())
                    .build();
        } else {
            return MemberResponseDto.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .build();
        }
    }
}
