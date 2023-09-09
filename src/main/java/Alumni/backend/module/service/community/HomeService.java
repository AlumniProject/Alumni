package Alumni.backend.module.service.community;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.MemberProfileDto;
import Alumni.backend.module.dto.community.PopularPostResponseDto;
import Alumni.backend.module.dto.community.SimpleContestDto;
import Alumni.backend.module.repository.community.post.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Alumni.backend.module.repository.contest.ContestRepository;
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
  private final ContestRepository contestRepository;

  public List<PopularPostResponseDto> findPopularPosts(Member member) {

    List<Long> list = postRepository.findByUniversityId(member.getUniversity().getId());
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
  public List<SimpleContestDto> findTop5Contest() {
    List<SimpleContestDto> simpleContestDtos = new ArrayList<>();

    List<Contest> contests = contestRepository.searchRecentContest();

    for (Contest contest : contests) {
      SimpleContestDto simpleContestDto = SimpleContestDto.getSimpleContestDto(contest);
      simpleContestDtos.add(simpleContestDto);
    }

    return simpleContestDtos;
  }

  public MemberProfileDto findMemberProfile(Member member) {
    Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));

    MemberProfileDto memberProfileDto = MemberProfileDto.memberProfileDto(findMember);

    return memberProfileDto;
  }
}
