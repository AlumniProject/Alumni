package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.dto.registration.MemberResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamCommentDto {

    private Long id;
    private String content;
    private MemberResponseDto writer;
    private List<TeamRecommentDto> recommentList;

    public static TeamCommentDto getCommentDto(Comment comment) {
        return TeamCommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(MemberResponseDto.getMemberResponseDto(comment.getMember()))
                .build();
    }

    public void setRecommentList(List<TeamRecommentDto> recommentList) {
        this.recommentList = recommentList;
    }
}
