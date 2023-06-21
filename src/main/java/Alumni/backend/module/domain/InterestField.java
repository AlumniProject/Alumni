package Alumni.backend.module.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class InterestField {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 15)
    private String fieldName;

}
