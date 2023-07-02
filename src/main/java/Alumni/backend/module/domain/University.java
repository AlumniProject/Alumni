package Alumni.backend.module.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
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
}