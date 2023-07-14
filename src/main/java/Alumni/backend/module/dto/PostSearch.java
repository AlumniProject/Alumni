package Alumni.backend.module.dto;

import lombok.Data;

@Data
public class PostSearch {

    private Long id; // 게시판 ID
    private String hashTag; // 기술게시판의 경우 해시태그로 검색
    private String content; // 검색어로 검색 - title or content
}