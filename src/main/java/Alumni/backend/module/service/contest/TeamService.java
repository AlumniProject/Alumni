package Alumni.backend.module.service.contest;

import Alumni.backend.infra.event.contest.*;
import Alumni.backend.infra.exception.BadRequestException;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.infra.response.TeamListResponse;
import Alumni.backend.module.domain.community.Comment;
import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.domain.contest.Teammate;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.CommentDto;
import Alumni.backend.module.dto.community.RecommentDto;
import Alumni.backend.module.dto.contest.*;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.contest.ContestRepository;
import Alumni.backend.module.repository.contest.TeamRepository;
import Alumni.backend.module.repository.contest.TeammateRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final ApplicationEventPublisher eventPublisher;

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
        team.teamModify(teamRequestDto);
        // 신청한 모든 사람들에게 수정 알림
        List<Member> members = teammateRepository.findByTeamIdFetchJoinMember(teamId).stream()
                .map(Teammate::getMember).collect(Collectors.toList());
        eventPublisher.publishEvent(new TeamModifyEvent(members, team));
    }

    public void teamDelete(Member member, Long teamId) {
        Team team = teamRepository.findByIdFetchJoinContest(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        if (!team.getMember().getId().equals(member.getId())) // 수정하는 사람이 작성한 팀모집인지 확인
            throw new IllegalArgumentException("Bad Request");

        // 달린 댓글 대댓글 삭제
        List<Comment> comments = commentRepository.findByTeamId(teamId);
        commentRepository.deleteAll(comments);
        // teammate 삭제
        List<Teammate> teammates = teammateRepository.findByTeamIdFetchJoinMember(teamId);
        List<Member> members = teammates.stream().map(Teammate::getMember).collect(Collectors.toList());
        teammateRepository.deleteAll(teammates);
        // team 삭제
        teamRepository.delete(team);
        // 신청한 모든 사람들에게 삭제 알림
        eventPublisher.publishEvent(new TeamDeleteEvent(members, team));
    }

    @Transactional(readOnly = true)
    public TeamResponseDto teamDetail(Long teamId) {
        Team team = teamRepository.findByIdFetchJoinMemberAndImage(teamId);
        if (team == null) { // 모집글이 없음
            throw new NoExistsException("NO_EXIST_TEAM");
        }
        // 댓글 제외하고 팀 모집글 response 작성
        TeamResponseDto teamResponseDto = TeamResponseDto.getTeamResponseDto(team,
                teammateRepository.countByTeamIdAndApproveIsTrue(teamId));
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
        Team team = teamRepository.findByIdFetchJoinMember(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        if (team.getClosed()) { // 마감했을 경우
            throw new BadRequestException("CLOSED");
        }
        Optional<Teammate> teammate = teammateRepository.findByMemberIdAndTeamId(member.getId(), teamId);
        if (teammate.isPresent()) {
            throw new BadRequestException("EXIST_TEAMMATE");
        }
        teammateRepository.save(Teammate.createTeammate(team, member));
        // 작성자에게 알림
        eventPublisher.publishEvent(new TeamApplyEvent(team.getMember(), team));
    }

    public void cancelTeam(Member member, Long teamId) {
        // 팀원이 승인된 팀원인지 여부 확인
        Teammate teammate = teammateRepository.findByMemberIdAndTeamIdFetchJoinTeam(member.getId(), teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAMMATE"));
        teammateRepository.delete(teammate);
    }

    public void approveTeam(Member member, Long teamId, TeamApproveDto teamApproveDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        // 작성자 맞는지 확인
        if (!team.getMember().getId().equals(member.getId()))
            throw new IllegalArgumentException("Bad Request");

        List<Long> memberIds = teamApproveDto.getMemberList();
        int size = memberIds.size();

        // total 넘어가는지 확인
        Integer currentNum = teammateRepository.countByTeamIdAndApproveIsTrue(teamId);
        if (currentNum == null)
            currentNum = 0;
        if (size > team.getHeadcount() - currentNum)
            throw new IllegalArgumentException("Bad Request");

        // 승인으로 변경
        teammateRepository.findByTeamIdAndMemberIdIn(teamId, memberIds).forEach(Teammate::approveTeammate);
        // 승인된 팀원들에게 알림
        eventPublisher.publishEvent(new TeamLeaderApproveEvent(memberRepository.findByIdIn(memberIds), team));
    }

    public void teamLeaderCancelMate(Member member, Long teamId, TeamLeaderCancelDto cancelDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        // 작성자 맞는지 확인
        if (!team.getMember().getId().equals(member.getId()))
            throw new IllegalArgumentException("Bad Request");
        // teammate의 approve false + team의 current 한명 삭제
        Teammate teammate = teammateRepository.findByMemberIdAndTeamId(cancelDto.getMemberId(), team.getId())
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAMMATE"));
        if (!teammate.getApprove()) // 이미 취소했는 경우 예외처리
            throw new IllegalArgumentException("Bad Request");
        teammate.cancelTeammate();
    }

    public void closedTeam(Member member, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        // 작성자 맞는지 확인
        if (!team.getMember().getId().equals(member.getId()))
            throw new IllegalArgumentException("Bad Request");
        team.closedTeam();
        // 승인된 팀원들에게 마감 알림
        List<Member> members = teammateRepository.findByTeamIdFetchJoinMemberWithApprove(teamId).stream()
                .map(Teammate::getMember).collect(Collectors.toList());
        eventPublisher.publishEvent(new TeamCloseEvent(members, team));
    }

    @Transactional(readOnly = true)
    public TeamListResponse allTeamList(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));

        // teammate에서 member, image, university 들고오기
        List<Teammate> teammates = teammateRepository.findByTeamIdFetchJoinMemberAndImageAndUniv(teamId);

        List<TeamApplyDto> teamApplyDtos = teammates.stream()
                .map(teammate -> {
                    List<String> fieldNames = teammate.getMember().getInterestFields().stream()
                            .map(interested -> interested.getInterestField().getFieldName()).collect(Collectors.toList());
                    return TeamApplyDto.getTeamApplyDto(teammate, fieldNames);
                }).collect(Collectors.toList());

        return new TeamListResponse(teamApplyDtos, team,
                teammateRepository.countByTeamIdAndApproveIsTrue(teamId), "SUCCESS");
    }

    public void deleteTeammateProcess(Member member) {
        List<Teammate> teammates = teammateRepository.findByMemberId(member.getId());
        if (!teammates.isEmpty()) {
            teammateRepository.deleteAll(teammates);
        }
    }

    public String teamRequest(Member leader, Long memberId, TeamInviteRequestDto teamInviteRequestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoExistsException("존재하지 않는 회원"));

        Team team = teamRepository.findById(teamInviteRequestDto.getTeamId())
                .orElseThrow(() -> new NoExistsException("존재하지 않는 팀"));

        Contest contest = contestRepository.findById(teamInviteRequestDto.getContestId())
                .orElseThrow(() -> new NoExistsException("존재하지 않는 공모전"));

        eventPublisher.publishEvent(new TeamInviteEvent(member, team.getId(), leader.getNickname(), contest.getTitle()));

        return "팀원 요청 완료";
    }
}
