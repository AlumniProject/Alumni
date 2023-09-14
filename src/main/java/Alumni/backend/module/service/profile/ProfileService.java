package Alumni.backend.module.service.profile;

import Alumni.backend.infra.exception.DuplicateNicknameException;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.Profile.MySkill;
import Alumni.backend.module.domain.Profile.Skill;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.registration.InterestField;
import Alumni.backend.module.domain.registration.Interested;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.profile.*;
import Alumni.backend.module.repository.community.PostLikeRepository;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.profile.FollowRepository;
import Alumni.backend.module.repository.profile.MySkillRepository;
import Alumni.backend.module.repository.profile.SkillRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final MySkillRepository mySkillRepository;
    private final SkillRepository skillRepository;

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

    public ProfilePostsResponseDto profilePosts(Member currentMember, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));

        HashMap<Long, Long> postMap = postLikeRepository.countPostLikesByPostId();
        HashMap<Long, Long> commentMap = commentRepository.countCommentsByPostId();

        Boolean isOwner = false;
        Boolean isFollow = false;

        if(currentMember.getId().equals(memberId))//내 프로필
        {
            isFollow = true;
            isOwner = true;
        }else{//다른 사람 프로필
            if(followRepository.findByFollowerIdAndFollowingId(currentMember.getId(), memberId).isPresent())//팔로우 하고 있는지
                isFollow = true;
        }

        ProfileResponseDto profileResponseDto = ProfileResponseDto.getProfileResponseDto(member, isOwner, isFollow);

        int follower = followRepository.countByFollowerId(memberId);
        int following = followRepository.countByFollowingId(memberId);

        List<MyPostResponseDto> posts = new ArrayList<>();
        List<Post> myPost = postRepository.findAllByMemberId(memberId);

        for (Post post : myPost) {
            Long likes = postMap.get(post.getId());
            Long comments = commentMap.get(post.getId());

            MyPostResponseDto myPosts = MyPostResponseDto.getMyPosts(post, likes, comments);

            myPosts.setHashTag(post.getPostTags().stream()
                    .map(postTag -> postTag.getTag().getName())
                    .collect(Collectors.toList()));

            posts.add(myPosts);
        }

        return ProfilePostsResponseDto.getProfilePostsResponseDto(profileResponseDto, follower, following, posts);
    }

    @Transactional
    public void editLink(Member member, Long memberId, String link) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));

        if(!member.getId().equals(findMember.getId()))//같은 사람인지 확인
            throw new IllegalArgumentException("Bad Request");

        if(link.contains("facebook"))
            findMember.editFacebook(link);
        else if(link.contains("instagram"))
            findMember.editInstagram(link);
        else if(link.contains("github"))
            findMember.editGithub(link);
        else
            throw new IllegalArgumentException("Bad Request");
    }


    @Transactional
    public void editSkill(Member currentMember, Long memberId, SkillRequestDto skillRequestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));
        List<MySkill> mySkillList =new ArrayList<>();

        if(!member.getId().equals(currentMember.getId()))//동일한 사람인지 확인
            throw new IllegalArgumentException("Bad Request");

        //기존 스킬 삭제
        if(mySkillRepository.findById(member.getId()).isPresent()){
            List<MySkill> deleteSkill = mySkillRepository.findByMemberId(member.getId());
            member.clearMySkills();
            mySkillRepository.deleteAll(deleteSkill);
        }

        //새로운 스킬 추가
        for(int i = 0; i<skillRequestDto.getSkill().size(); i++){
            Skill skill = skillRepository.findBySkillName(skillRequestDto.getSkill().get(i))
                    .orElseThrow(() -> new NoExistsException("존재하지 않는 스킬"));

            MySkill mySkill = MySkill.createMySkill(member, skill);
            mySkill.addMemberSkills(member);
            mySkillList.add(mySkill);
        }

        mySkillRepository.saveAll(mySkillList);
    }
}
