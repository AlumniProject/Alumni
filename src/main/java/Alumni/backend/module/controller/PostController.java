package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.GeneralResponse;
import Alumni.backend.infra.response.PostSearchResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.dto.PostResponseDto;
import Alumni.backend.module.dto.requestDto.PostSearch;
import Alumni.backend.module.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/all")
    public ResponseEntity<? extends BasicResponse> findAllPost(@CurrentUser Member member) {
        List<String> tagRankList = postService.tagRank();
        List<PostResponseDto> postResponseDtos = postService.findAllPosts(member);
        return ResponseEntity.ok()
                .body(new PostSearchResponse<>(postResponseDtos, tagRankList, "모든 게시글 조회 완료"));
    }

    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> postSearch(@CurrentUser Member member,
                                                              @ModelAttribute PostSearch postSearch) {
        return ResponseEntity.ok().body(postService.search(member, postSearch));
    }

    @GetMapping("/view/{postId}")
    public ResponseEntity<? extends BasicResponse> viewPostDetail(@PathVariable("postId") Long postId) {
        PostResponseDto postDetails = postService.getPostDetails(postId);
        return ResponseEntity.ok().body(new GeneralResponse<>(postDetails, "게시글 상세보기 전송 완료"));
    }
}
