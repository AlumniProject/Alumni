package Alumni.backend.module.dto.community;

import Alumni.backend.module.domain.community.Tag;
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
