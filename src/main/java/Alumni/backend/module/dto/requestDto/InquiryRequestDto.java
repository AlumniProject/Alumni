package Alumni.backend.module.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InquiryRequestDto {
    @Schema(description = "문의 내용", example = "abcd1234@yu.ac.kr 이메일이 없어요 넣어주세요")
    private String content;
}
