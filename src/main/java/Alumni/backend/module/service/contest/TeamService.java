package Alumni.backend.module.service.contest;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.domain.contest.Teammate;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.CommentDto;
import Alumni.backend.module.dto.community.RecommentDto;
import Alumni.backend.module.dto.community.TeamApproveDto;
import Alumni.backend.module.dto.contest.TeamRequestDto;
import Alumni.backend.module.dto.contest.TeamResponseDto;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.contest.ContestRepository;
import Alumni.backend.module.repository.contest.TeamRepository;
import Alumni.backend.module.repository.contest.TeammateRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;
    private final CommentRepository commentRepository;
    private final TeammateRepository teammateRepository;
    private final MemberRepository memberRepository;

    public void saveDummyContest() {
        Contest contest = Contest.builder()
                .link("www.test.com")
                .field("IT")
                .title("testTitle")
                .content("testContent")
                .poster("poster.jpg")
                .likeNum(0)
                .teamNum(5)
                .build();
        contestRepository.save(contest);
    }

    public void createTeam(Member member, Long contestId, TeamRequestDto teamRequestDto) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_CONTEST"));
        teamRepository.save(Team.createTeam(teamRequestDto, member, contest));
    }

    public void modifyTeam(Member member, Long teamId, TeamRequestDto teamRequestDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        if (!team.getMember().getId().equals(member.getId())) // 수정하는 사람이 작성한 팀모집인지 확인
            throw new IllegalArgumentException("Bad Request");
        //TODO : 수정 이전에 이미 팀원으로 승인된 회원들 처리 + 팀원들에게 알림 처리
        team.teamModify(teamRequestDto);
    }

    public void teamDelete(Member member, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        if (!team.getMember().getId().equals(member.getId())) // 수정하는 사람이 작성한 팀모집인지 확인
            throw new IllegalArgumentException("Bad Request");
        //TODO : teammate 테이블에서 관련 데이터 삭제 + 달린 댓글 삭제 + 알림처리 할건지
        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    public TeamResponseDto teamDetail(Long teamId) {
        Team team = teamRepository.findByIdFetchJoinMemberAndImage(teamId);
        if (team == null) {
            throw new NoExistsException("NO_EXIST_TEAM");
        }
        // 댓글 제외하고 팀 모집글 response 작성
        TeamResponseDto teamResponseDto = TeamResponseDto.getTeamResponseDto(team);
        // 댓글 확인
        List<CommentDto> commentDtos = new ArrayList<>();
        commentRepository.findByTeamIdFetchJoinMemberAndImage(team.getId()).forEach(comment -> {
            if (comment.getParent() == null) { // 대댓글이 아닌 경우만
                CommentDto commentDto = CommentDto.getTeamCommentDto(comment);
                // recommentList 확인
                List<RecommentDto> recommentDtos = comment.getChildren().stream()
                        .map(RecommentDto::getTeamRecommentDto).collect(Collectors.toList());
                commentDto.setRecommentList(recommentDtos);
                commentDtos.add(commentDto);
            }
        });
        teamResponseDto.setCommentList(commentDtos);
        return teamResponseDto;
    }

    public void applyTeam(Member member, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        //TODO : 작성자에게 알림
        teammateRepository.save(Teammate.createTeammate(team, member));
    }

    public void cancelTeam(Member member, Long teamId) {
        // 팀원이 승인된 팀원인지 여부 확인
        Teammate teammate = teammateRepository.findByMemberIdAndTeamIdFetchJoinTeam(member.getId(), teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAMMATE"));
        Boolean approve = teammate.getApprove();
        if (approve) { // 승인된 팀원의 경우
            teammate.getTeam().cancelTeam(1);
        }
        teammateRepository.delete(teammate);
    }

    public void approveTeam(Member member, Long teamId, TeamApproveDto teamApproveDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        if (!team.getMember().getId().equals(member.getId()))
            throw new IllegalArgumentException("Bad Request");

        List<String> nicknameList = teamApproveDto.getMemberList();
        int size = nicknameList.size();
        // total 넘어가는지 확인
        if (nicknameList.isEmpty() || size > team.getHeadcount() - team.getCurrent())
            throw new IllegalArgumentException("Bad Request");

        List<Member> members = memberRepository.findByNicknameIn(nicknameList);
        if (size != members.size())
            throw new NoExistsException("NO_EXIST_MEMBER");
        teammateRepository.findByTeamIdAndMemberIn(teamId, members).forEach(Teammate::approveTeammate);
        team.approveTeam(members.size());
        //TODO : 승인된 팀원들에게 알림
    }
}
