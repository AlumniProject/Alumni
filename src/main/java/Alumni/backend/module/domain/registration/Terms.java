package Alumni.backend.module.domain.registration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Terms {//약관 table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    public static Terms createTerms(String title, String content) {
        Terms terms = new Terms();
        terms.title = title;
        terms.content = content;
        return terms;
    }
}
