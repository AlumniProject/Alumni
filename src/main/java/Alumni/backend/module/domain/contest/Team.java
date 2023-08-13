package Alumni.backend.module.domain.contest;

import Alumni.backend.module.domain.BaseTimeEntity;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.contest.TeamRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false)
    private String region; // 활동지역

    @Column(nullable = false)
    private Integer headcount; // 총 인원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    public static Team createTeam(TeamRequestDto teamRequestDto, Member member, Contest contest) {
        return Team.builder()
                .title(teamRequestDto.getTitle())
                .content(teamRequestDto.getContent())
                .region(teamRequestDto.getRegion())
                .headcount(teamRequestDto.getTotal())
                .member(member)
                .contest(contest)
                .build();
    }

    public void teamModify(TeamRequestDto teamRequestDto) {
        this.title = teamRequestDto.getTitle();
        this.region = teamRequestDto.getRegion();
        this.headcount = teamRequestDto.getTotal();
        this.content = teamRequestDto.getContent();
    }
}
