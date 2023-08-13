package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.dto.registration.MemberResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamRecommentDto {

    private Long id;
    private String content;
    private MemberResponseDto writer;

    public static TeamRecommentDto getTeamRecommentDto(Comment comment) {
        return TeamRecommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(MemberResponseDto.getMemberResponseDto(comment.getMember()))
                .build();
    }
}
