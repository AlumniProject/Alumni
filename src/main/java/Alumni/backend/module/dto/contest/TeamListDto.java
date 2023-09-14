package Alumni.backend.module.dto.contest;

import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.dto.registration.MemberResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamListDto {
    private Long id;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createTime;
    private String region;
    private Integer total;
    private Long current;
    private MemberResponseDto writer;

    public static TeamListDto teamListDto(Team team, Long current){
        return TeamListDto.builder()
                .id(team.getId())
                .title(team.getTitle())
                .createTime(team.getCreateTime())
                .region(team.getRegion())
                .total(team.getHeadcount())
                .current(current == null ? 0 : current)
                .writer(MemberResponseDto.getMemberResponseDto(team.getMember()))
                .build();
    }
}
