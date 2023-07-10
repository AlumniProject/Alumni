package Alumni.backend.module.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "black_list_id", nullable = false)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private String accessToken;

    public static BlackList createBlackList(String accessToken) {
        BlackList blackList = new BlackList();
        blackList.createAt = LocalDateTime.now();
        blackList.accessToken = accessToken;
        return blackList;
    }
}
