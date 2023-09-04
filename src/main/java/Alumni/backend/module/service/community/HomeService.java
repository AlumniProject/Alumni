package Alumni.backend.module.service.community;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.dto.community.HomeDto;
import Alumni.backend.module.dto.community.MemberProfileDto;
import Alumni.backend.module.dto.community.PopularPostResponseDto;
import Alumni.backend.module.repository.community.post.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

  private final PostRepository postRepository;
  private final MemberRepository memberRepository;

  public HomeDto findPopularPosts(Member member) {
    HomeDto homeDto = new HomeDto();

    Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));
    MemberProfileDto profile = MemberProfileDto.memberProfileDto(findMember);

    List<Post> popularPosts = postRepository.findPopularPosts();

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
    homeDto.setHomeDto(profile,popularPostResponseDtos);
    return homeDto;
  }
}
