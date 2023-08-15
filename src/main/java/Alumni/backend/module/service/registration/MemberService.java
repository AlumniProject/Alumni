package Alumni.backend.module.service.registration;

import Alumni.backend.infra.jwt.JwtService;
import Alumni.backend.infra.principal.PrincipalDetails;
import Alumni.backend.module.domain.*;
import Alumni.backend.module.domain.community.CommentLike;
import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.domain.community.PostLike;
import Alumni.backend.module.domain.registration.*;
import Alumni.backend.module.dto.registration.SignUpRequestDto;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.infra.exception.DuplicateNicknameException;
import Alumni.backend.module.repository.*;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.CommentLikeRepository;
import Alumni.backend.module.repository.community.PostLikeRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.registration.*;
import Alumni.backend.module.service.FileService;
import Alumni.backend.module.service.community.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final UniversityRepository universityRepository;
    private final InquiryRepository inquiryRepository;
    private final TermsRepository termsRepository;
    private final InterestFieldRepository interestFieldRepository;
    private final InterestedRepository interestedRepository;
    private final ImageRepository imageRepository;
    private final FileService fileService;
    private final JwtService jwtService;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final EntityManager em;

    public void login(Member member, HttpServletResponse response) {
        // 회원가입 후 자동로그인

        // UsernamePassword 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                new PrincipalDetails(member),
                null
        );
        SecurityContextHolder.getContext().setAuthentication(
                authenticationToken
        );

        jwtService.createAllTokenAddHeader(member, response);
    }

    public void signUp(SignUpRequestDto memberInfo, HttpServletResponse response) {
        if (!verifiedEmailRepository.findByEmail(memberInfo.getEmail()).get().isVerified()) {
            throw new NoExistsException("검증되지 않은 이메일입니다.");
        }

        if (memberRepository.existsMemberByNickname(memberInfo.getNickname()))
            throw new DuplicateNicknameException("이미 존재하는 닉네임입니다.");

        String email = memberInfo.getEmail();
        int index = email.indexOf("@");
        String univEmail = email.substring(index);

        University findUniversity = universityRepository.findByUnivEmail1OrUnivEmail2(univEmail, univEmail);

        Member member = Member.createMember(
                memberInfo.getEmail(), memberInfo.getNickname(), memberInfo.getClassOf(),
                memberInfo.getMajor(), findUniversity, memberInfo.getFcmToken());
        memberRepository.save(member);

        // 회원가입 후 자동로그인
        login(member, response);
    }

    //문의하기
    public void SaveInquiry(String content) {
        Inquiry inquiry = Inquiry.createInquiry(content);
        inquiryRepository.save(inquiry);
    }

    public List<Terms> findTerms() {
        return termsRepository.findAll();
    }

    public void updateInterest(List<String> interestNames, Long memberId) {

        //회원 검증
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistsException("사용자가 존재하지 않습니다"));

        //기존 관심사항 삭제
        List<Interested> deleteInterested = interestedRepository.findByMemberId(findMember.getId());
        findMember.clearInterestFields();
        interestedRepository.deleteAll(deleteInterested);

        //관심사항 검증
        for (int i = 0; i < interestNames.size(); i++) {
            InterestField interestField = interestFieldRepository.findByFieldName(interestNames.get(i)).
                    orElseThrow(() -> new NoExistsException("field 가 존재하지 않습니다."));

            //새로운 관심사항 업데이트
            Interested interested = Interested.createInterested(findMember, interestField);
            interested.addMemberInterestedFields(findMember);
            interestedRepository.save(interested);
        }
    }

    //이미지 update
    public void uploadProfileImage(Long memberId, String storageImageName) {
        //기존 이미지 Id
        Optional<Image> oldImage = memberRepository.findImageByMemberId(memberId);

        //새로운 프로필 설정
        Image profileImage = imageRepository.findByStorageImageName(storageImageName).orElseThrow(() -> new NoExistsException("Bad Request"));
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new NoExistsException("Bad Request"));
        findMember.uploadProfile(profileImage);

        //기존 이미지 삭제
        if (oldImage.isPresent()) {
            imageRepository.delete(oldImage.get());//db에서 삭제
            fileService.deleteFile(oldImage.get().getStorageImageName());//s3에서 삭제
        }
    }

    // 회원 탈퇴
    public void deleteMember(Member user) {
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));
        // 프로필 이미지 삭제
        if (member.getProfileImage() != null) {
            imageRepository.deleteById(member.getProfileImage().getId());
        }

        // postLike, commentLike 삭제
        List<PostLike> postLikes = member.getPostLikes();
        List<CommentLike> commentLikes = member.getCommentLikes();
        if (!postLikes.isEmpty()) {
            postLikeRepository.deleteAll(member.getPostLikes());
        }
        if (!commentLikes.isEmpty()) {
            commentLikeRepository.deleteAll(member.getCommentLikes());
        }
        // 회원이 작성한 댓글 삭제
        commentRepository.deleteAllByMemberId(member.getId());
        // 회원이 설정한 관심분야 삭제
        interestedRepository.deleteAllByMemberId(member.getId());

        // 회원이 작성한 게시글 + 기술게시글이면 해시태그 삭제 + 게시글에 달린 댓글과 좋아요 삭제
        List<Post> posts = postRepository.findAllByMemberId(member.getId());

        em.flush();
        em.clear();

        for (Post post : posts) {
            postService.postDelete(member, post.getId());
        }
        VerifiedEmail verifiedEmail = verifiedEmailRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));
        verifiedEmail.verifiedFalse();
        memberRepository.delete(member);
    }
}