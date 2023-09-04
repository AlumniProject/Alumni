package Alumni.backend.module.service.Profile;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.Profile.Follow;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.repository.Profile.FollowRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public String follow(Member currentMember, Long memberId) {

        if(followRepository.findByFollowerIdAndFollowingId(currentMember.getId(), memberId).isPresent()) {
            Optional<Follow> findFollow = followRepository.findByFollowerIdAndFollowingId(currentMember.getId(), memberId);

            if (!findFollow.get().getFollower().getId().equals(currentMember.getId()))//팔로우 한 사람이랑 취소하는 사람이랑 같은 사람인지 확인
                throw new IllegalArgumentException("Bad Request");

            Follow follow = findFollow.get();
            followRepository.delete(follow);//팔로우 취소(삭제)

            return "팔로우 취소 완료";
        }

        Member followMember = memberRepository.findById(memberId).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));

        Follow follow = Follow.createFollow(currentMember, followMember);
        followRepository.save(follow);

        return "팔로우 완료";
    }
}
