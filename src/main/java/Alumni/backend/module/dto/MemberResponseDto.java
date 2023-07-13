package Alumni.backend.module.dto;

import lombok.*;

@Data
@Builder
public class MemberResponseDto {

    private Long id;
    private String nickname;
    private String imagePath;
}
