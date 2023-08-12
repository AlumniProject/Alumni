package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Contest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestResponseDto {
    private Long contestId;
    private String title;
    private String content;
    private String field;
    private Integer likes;
    private Integer teams;

    public static ContestResponseDto getContestResponseDto(Contest contest){
        return ContestResponseDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .content(contest.getContent())
                .field(contest.getField())
                .likes(contest.getLikeNum())
                .teams(contest.getTeamNum())
                .build();
    }
}
