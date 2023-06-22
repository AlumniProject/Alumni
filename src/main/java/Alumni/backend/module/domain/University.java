package Alumni.backend.module.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class University {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id", nullable = false)
    private Long id;

    @Column(name = "univ_name", nullable = false, length = 10)
    private String name;

    @Column(name = "univ_email", nullable = false, length = 30)
    private String univEmail;

}