package Alumni.backend.module.service.profile;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.profile.FriendDto;
import Alumni.backend.module.dto.profile.ProfileResponseDto;
import Alumni.backend.module.repository.profile.FollowRepository;
import Alumni.backend.module.repository.registration.InterestedRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final InterestedRepository interestedRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;


    public List<FriendDto> findFriends(Member member) {
        Member currentMember = memberRepository.findById(member.getId()).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));
        List<String> currentInterestedList = interestedRepository.findInterestedById(currentMember.getId()).orElseThrow(() -> new NoExistsException("관심분야 없음"));

        List<Long> followingList = followRepository.findByFollowerId(currentMember.getId());

        double fieldNum = currentInterestedList.size();

        List<FriendDto> friends = new ArrayList<>();
        Map<FriendDto, Double> percentMap = new HashMap<>();

        Map<Member, List<String>> interestedMap = interestedRepository.findInterestedMemberId();

        for (Member m : interestedMap.keySet()) {
            if(m.getId().equals(currentMember.getId())) continue;
            List<String> interestedFieldList = interestedMap.get(m);

            double sameFieldNum = interestedFieldList.stream()
                    .filter(currentInterestedList::contains).count(); //같은 필드 몇개인지 찾기1

            //퍼센트 계산
            double percent = (sameFieldNum/fieldNum) * 100.0;

            if(percent >= 65.0){
                boolean followStatus = false;

                ProfileResponseDto profileResponseDto = ProfileResponseDto.getProfileResponseDto(m);

                if(followingList.contains(m.getId()))
                    followStatus = true;

                FriendDto friendDto = FriendDto.getFriendDto(profileResponseDto, followStatus, interestedFieldList);
                percentMap.put(friendDto, percent);
            }
        }

        //퍼센트를 기준으로 내림차순 정렬
        Map<FriendDto, Double> sortedPercentMap = percentMap.entrySet()
                .stream()
                .sorted(Map.Entry.<FriendDto, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        for (FriendDto friendDto : sortedPercentMap.keySet()) {
            friends.add(friendDto);
        }

        return friends;
    }
}
