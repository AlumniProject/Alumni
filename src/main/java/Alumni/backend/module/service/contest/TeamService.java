package Alumni.backend.module.service.contest;

import Alumni.backend.infra.exception.BadRequestException;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.infra.response.TeamListResponse;
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
import lombok.RequiredArgsConstructor;
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

    public void saveDummyContest() {
        Contest contest = Contest.builder()
                .link("www.test.com")
                .field("IT")
                .title("testTitle")
                .content("testContent")
                .poster("poster.jpg")
                .likeNum(0)
                .teamNum(0)
                .build();
        contestRepository.save(contest);
    }

    public void createTeam(Member member, Long contestId, TeamRequestDto teamRequestDto) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_CONTEST"));
        teamRepository.save(Team.createTeam(teamRequestDto, member, contest));
        contest.updateTeamNum(contest.getTeamNum() + 1); // 팀 모집글 수 + 1
    }

    public void modifyTeam(Member member, Long teamId, TeamRequestDto teamRequestDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        if (!team.getMember().getId().equals(member.getId())) // 수정하는 사람이 작성한 팀모집인지 확인
            throw new IllegalArgumentException("Bad Request");
        team.teamModify(teamRequestDto);
        //TODO : 수정 이전에 이미 팀원으로 승인된 회원들 처리 + 팀원들에게 알림 처리
    }

    public void teamDelete(Member member, Long teamId) {
        Team team = teamRepository.findByIdFetchJoinContest(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        if (!team.getMember().getId().equals(member.getId())) // 수정하는 사람이 작성한 팀모집인지 확인
            throw new IllegalArgumentException("Bad Request");
        teamRepository.delete(team);
        team.getContest().updateTeamNum(team.getContest().getTeamNum() - 1);
        //TODO : teammate 테이블에서 관련 데이터 삭제 + 달린 댓글 삭제 + 알림처리 할건지
    }

    @Transactional(readOnly = true)
    public TeamResponseDto teamDetail(Long teamId) {
        Team team = teamRepository.findByIdFetchJoinMemberAndImage(teamId);
        if (team == null) { // 모집글이 없음
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
        if (team.getClosed()) { // 마감했을 경우
            throw new BadRequestException("CLOSED");
        }
        Optional<Teammate> teammate = teammateRepository.findByMemberIdAndTeamId(member.getId(), teamId);
        if (teammate.isPresent()) {
            throw new BadRequestException("EXIST_TEAMMATE");
        }
        teammateRepository.save(Teammate.createTeammate(team, member));
        //TODO : 작성자에게 알림
    }

    public void cancelTeam(Member member, Long teamId) {
        // 팀원이 승인된 팀원인지 여부 확인
        Teammate teammate = teammateRepository.findByMemberIdAndTeamIdFetchJoinTeam(member.getId(), teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAMMATE"));
        if (teammate.getApprove()) { // 승인된 팀원의 경우
            teammate.getTeam().updateCurrent(teammate.getTeam().getCurrent() - 1);
        }
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
        if (size > team.getHeadcount() - team.getCurrent())
            throw new IllegalArgumentException("Bad Request");

        // 승인으로 변경
        teammateRepository.findByTeamIdAndMemberIdIn(teamId, memberIds).forEach(Teammate::approveTeammate);
        team.updateCurrent(team.getCurrent() + size);
        //TODO : 승인된 팀원들에게 알림
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
        teammate.cancelTeammate();
        team.updateCurrent(team.getCurrent() - 1);
    }

    public void closedTeam(Member member, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoExistsException("NO_EXIST_TEAM"));
        // 작성자 맞는지 확인
        if (!team.getMember().getId().equals(member.getId()))
            throw new IllegalArgumentException("Bad Request");
        team.closedTeam();
        // TODO : 승인된 팀원들에게 마감 알림
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

        return new TeamListResponse(teamApplyDtos, team, "SUCCESS");
    }
}
