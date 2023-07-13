package Alumni.backend.module.service;

import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Post;
import Alumni.backend.module.domain.Tag;
import Alumni.backend.module.dto.MemberResponseDto;
import Alumni.backend.module.dto.PostResponseDto;
import Alumni.backend.module.repository.PostRepository;
import Alumni.backend.module.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllPosts(Member user) {
        // post + member + image fetch join
        List<Post> posts = postRepository.findAllPosts();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for (Post post : posts) {
            if (post.getBoard().getId() == 2
                    && !post.getMember().getUniversity().getId().equals(user.getUniversity().getId())) {
                continue;
            }

            PostResponseDto postResponseDto = PostResponseDto.builder()
                    .boardId(post.getBoard().getId())
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createTime(post.getCreateTime())
                    .build();

            // hashTag 확인
            if (post.getBoard().getId() == 3 && post.getPostTags() != null) {
                postResponseDto.setHashTag(post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                        .map(postTag -> postTag.getTag().getName())
                        .collect(Collectors.toList()));
            }
            // writer 이미지 확인
            if (post.getMember().getProfileImage() != null) {
                postResponseDto.setWriter(MemberResponseDto.builder()
                        .id(post.getMember().getId())
                        .nickname(post.getMember().getNickname())
                        .imagePath(post.getMember().getProfileImage().getImagePath())
                        .build());
            } else {
                postResponseDto.setWriter(MemberResponseDto.builder()
                        .id(post.getMember().getId())
                        .nickname(post.getMember().getNickname())
                        .build());
            }
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    // 가장 많은 태그 리스트 조회
    @Transactional(readOnly = true)
    public List<String> tagRank() {
        return tagRepository.findTop5ByOrderByCountDesc().stream()
                .map(Tag::getName).collect(Collectors.toList());
    }
}
