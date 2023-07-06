package Alumni.backend.module.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long id;
    private String originalImageName;
    private String storageImageName;
    private String imagePath;


    public Image(String originalImageName, String storageImageName, String imagePath) {
        this.originalImageName = originalImageName;
        this.storageImageName = storageImageName;
        this.imagePath = imagePath;
    }
}
