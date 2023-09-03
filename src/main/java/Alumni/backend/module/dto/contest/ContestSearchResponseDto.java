package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Contest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestSearchResponseDto {
    private Long contestId;
    private String title;
    private String content;
    private String field;
    private String poster;
    private Integer likes;
    private Integer teams;

    public static ContestSearchResponseDto contestSearchResponseDto(Contest contest, Integer likeNum, Integer teamNum) {
        return ContestSearchResponseDto.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .content(contest.getContent())
                .field(contest.getField())
                .poster(contest.getPoster())
                .likes(likeNum)
                .teams(teamNum)
                .build();
    }
}
