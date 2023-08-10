package Alumni.backend.module.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String place;
    private String date;
    private String singer;
    private String time;
    private String maxPrice;
    private String minPrice;
}
