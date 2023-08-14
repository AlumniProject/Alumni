package Alumni.backend.module.dto.community;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostSearch {

    private Long id; // 게시판 ID

    @Size(max = 5)
    private List<String> hashTag; // 기술게시판의 경우 해시태그로 검색

    private String content; // 검색어로 검색 - title or content
}