package Alumni.backend.module.dto;

import Alumni.backend.module.domain.Tag;
import lombok.Data;

@Data
public class TagDto {

    private Long id;
    private String name;

    public TagDto(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }
}
