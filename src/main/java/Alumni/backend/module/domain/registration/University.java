package Alumni.backend.module.domain.registration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id", nullable = false)
    private Long id;

    @Column(name = "univ_name", nullable = false)
    private String name;

    @Column(name = "univ_email1")
    private String univEmail1;

    @Column(name = "univ_email2")
    private String univEmail2;

    @Builder
    private University(String name, String univEmail1, String univEmail2) {
        this.name = name;
        this.univEmail1 = univEmail1;
        this.univEmail2 = univEmail2;
    }
}