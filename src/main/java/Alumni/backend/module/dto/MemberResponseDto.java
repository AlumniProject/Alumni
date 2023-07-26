package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Member;
import lombok.*;

@Data
@Builder
public class MemberResponseDto {

    private Long id;
    private String nickname;
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
