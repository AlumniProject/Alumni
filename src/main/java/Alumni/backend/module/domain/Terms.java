package Alumni.backend.module.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Terms {//약관 table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;
}
