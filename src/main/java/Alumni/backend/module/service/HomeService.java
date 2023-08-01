package Alumni.backend.module.service;

import Alumni.backend.module.domain.Member;
import Alumni.backend.module.domain.Post;
import Alumni.backend.module.dto.PopularPostResponseDto;
import Alumni.backend.module.repository.Post.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

  private final PostRepository postRepository;

  public List<PopularPostResponseDto> findPopularPosts(Member member) {

    List<Long> list = postRepository.findByMemberId(member.getId());
    List<Post> popularPosts = postRepository.findPopularPosts(list);
    List<PopularPostResponseDto> popularPostResponseDtos = new ArrayList<>();

    for (Post popularPost : popularPosts) {

      PopularPostResponseDto popularPostResponseDto = PopularPostResponseDto.getPopularPostResponseDto(popularPost);

      //기술 게시판인 경우
      if (popularPost.getBoard().getId() == 3 && !popularPost.getPostTags().isEmpty()) {
        popularPostResponseDto.setHashTag(popularPost.getPostTags().stream()
            .map(postTag -> postTag.getTag().getName())
            .collect(Collectors.toList()));
      }

      popularPostResponseDtos.add(popularPostResponseDto);
    }

    return popularPostResponseDtos;
  }
}
