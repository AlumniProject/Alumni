package Alumni.backend.module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TermsDto {
    private Long id;
    private String title;
    private String content;
}
