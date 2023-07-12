package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.BasicResponse;
import Alumni.backend.infra.response.SingleResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.dto.requestDto.PostCreateRequestDto;
import Alumni.backend.module.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post/create")
    public ResponseEntity<? extends BasicResponse> postCreate(@CurrentUser Member member, @RequestBody @Valid PostCreateRequestDto postCreateRequestDto){
        postService.postCreate(member, postCreateRequestDto);

        return ResponseEntity.ok().body(new SingleResponse("게시글 등록 완료"));
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<? extends  BasicResponse> postDelete(@CurrentUser Member member, @PathVariable("id") Long postId){
        postService.postDelete(member, postId);

        return ResponseEntity.ok().body(new SingleResponse("게시글 삭제 완료"));
    }
}

