package Alumni.backend.module.domain.community;

import Alumni.backend.module.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long id;

    @Column(name = "board_name", nullable = false)
    private String name;
}
