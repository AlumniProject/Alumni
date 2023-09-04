package Alumni.backend.module.service.profile;

import Alumni.backend.infra.exception.DuplicateNicknameException;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.profile.NicknameDto;
import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    @Transactional
    public void editNickname(Member member, Long memberId, NicknameDto nicknameDto) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));

        if(!member.getId().equals(findMember.getId()))//같은 사람인지 확인
            throw new IllegalArgumentException("Bad Request");

        if(memberRepository.existsMemberByNickname(nicknameDto.getNickname()))
            throw new DuplicateNicknameException("이미 존재하는 닉네임입니다.");

        findMember.editNickname(nicknameDto.getNickname());
    }

    @Transactional
    public void editIntroduction(Member member, Long memberId, String introduction) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));

        if(!member.getId().equals(findMember.getId()))
            throw  new IllegalArgumentException("Bad Request");

        findMember.editIntroduction(introduction);
    }
}
