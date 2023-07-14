package Alumni.backend.module.service;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.infra.response.PostSearchResponse;
import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Post;
import Alumni.backend.module.domain.Tag;
import Alumni.backend.module.dto.MemberResponseDto;
import Alumni.backend.module.dto.PostResponseDto;
import Alumni.backend.module.dto.requestDto.PostSearch;
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
    public PostSearchResponse<?> search(Member user, PostSearch postSearch) {
        List<Post> posts = postRepository.searchPost(postSearch);
        List<String> tagRankList = null;
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        
        if (postSearch.getId() == 2) {
            if (!postSearch.getHashTag().isEmpty()) {
                throw new IllegalArgumentException("Bad Request");
            }
            for (Post post : posts) {
                if (!post.getMember().getUniversity().getId().equals(user.getUniversity().getId())) {
                    continue;
                }
                createPostResponseDtoWithProfileImage(postResponseDtos, post);
            }
        } else if (postSearch.getId() == 3) {
            tagRankList = tagRank();
            for (Post post : posts) {
                if (postSearch.getHashTag() != null) { // 검색 해시태그 있는 경우
                    if (post.getPostTags().isEmpty()) {
                        continue;
                    }
                    List<String> postTagList = post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                            .map(postTag -> postTag.getTag().getName())
                            .collect(Collectors.toList());
                    int size = postSearch.getHashTag().size();
                    for (String postSearchHashTag : postSearch.getHashTag()) {
                        if (postTagList.contains(postSearchHashTag)) {
                            size--;
                        }
                    }
                    if (size == 0) {
                        PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post);
                        postResponseDto.setHashTag(postTagList);
                        checkProfileImage(postResponseDtos, post, postResponseDto);
                    }
                } else { // 검색 해시태그 없는 경우
                    PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post);
                    if (!post.getPostTags().isEmpty()) {
                        postResponseDto.setHashTag(post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                                .map(postTag -> postTag.getTag().getName())
                                .collect(Collectors.toList()));
                    } else {
                        postResponseDto.setHashTag(null);
                    }
                    checkProfileImage(postResponseDtos, post, postResponseDto);
                }
            }
        } else {
            if (postSearch.getHashTag() != null) {
                throw new IllegalArgumentException("Bad Request");
            }
            for (Post post : posts) {
                createPostResponseDtoWithProfileImage(postResponseDtos, post);
            }
        }
        return new PostSearchResponse<>(postResponseDtos, tagRankList, "게시글 검색 결과 전송 완료");
    }

    private void createPostResponseDtoWithProfileImage(List<PostResponseDto> postResponseDtos, Post post) {
        PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post);
        // writer 이미지 확인
        checkProfileImage(postResponseDtos, post, postResponseDto);
    }

    private void checkProfileImage(List<PostResponseDto> postResponseDtos, Post post, PostResponseDto postResponseDto) {
        checkProfileExists(post, postResponseDto);
        postResponseDtos.add(postResponseDto);
    }

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

            PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post);

            // hashTag 확인
            if (post.getBoard().getId() == 3 && post.getPostTags() != null) {
                postResponseDto.setHashTag(post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                        .map(postTag -> postTag.getTag().getName())
                        .collect(Collectors.toList()));
            }
            // writer 이미지 확인
            checkProfileImage(postResponseDtos, post, postResponseDto);
        }
        return postResponseDtos;
    }

    // 가장 많은 태그 리스트 조회
    @Transactional(readOnly = true)
    public List<String> tagRank() {
        return tagRepository.findTop5ByOrderByCountDesc().stream()
                .map(Tag::getName).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostDetails(Long postId) {
        Post post = postRepository.findByIdFetchJoin(postId);
        if (post == null) {
            throw new NoExistsException("존재하지 않는 게시글입니다");
        }
        PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post);
        // hashTag 확인
        if (!post.getPostTags().isEmpty()) {
            postResponseDto.setHashTag(post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                    .map(postTag -> postTag.getTag().getName())
                    .collect(Collectors.toList()));
        } else {
            postResponseDto.setHashTag(null);
        }
        // writer 이미지 확인
        checkProfileExists(post, postResponseDto);
        return postResponseDto;
    }

    private void checkProfileExists(Post post, PostResponseDto postResponseDto) {
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
                    .imagePath(null)
                    .build());
        }
    }
}
