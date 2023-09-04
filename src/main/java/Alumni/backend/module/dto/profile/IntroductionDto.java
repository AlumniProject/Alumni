package Alumni.backend.module.dto.profile;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class IntroductionDto {
    @NotEmpty
    private String introduction;
}
