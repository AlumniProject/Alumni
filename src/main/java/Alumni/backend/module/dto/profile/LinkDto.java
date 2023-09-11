package Alumni.backend.module.dto.profile;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LinkDto {
    @NotNull @NotEmpty
    private String link;
}
