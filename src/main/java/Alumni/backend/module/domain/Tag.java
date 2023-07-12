package Alumni.backend.module.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Long id;

    private String name;
    private Integer count;

    public static Tag createTag(String name){
        Tag tag = new Tag();

        tag.name = name;
        tag.count = 1;

        return tag;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
