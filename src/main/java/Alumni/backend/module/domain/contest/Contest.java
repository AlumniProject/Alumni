package Alumni.backend.module.domain.contest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private String poster;

    @Column(nullable = false)
    private Integer likeNum;

    @Column(nullable = false)
    private Integer teamNum;

    public void updateTeamNum(Integer teamNum) {
        this.teamNum = teamNum;
    }
}
