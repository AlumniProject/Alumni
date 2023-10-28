package Alumni.backend.infra.event.contest;

import Alumni.backend.infra.notification.NotificationService;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.domain.registration.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Async
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleTeamModifyEvent(TeamModifyEvent teamModifyEvent) {
        Team team = teamModifyEvent.getTeam();
        multiMessageEvent(teamModifyEvent.getMembers(),
                "팀원 모집글 <" + team.getTitle() + ">이 수정되었습니다",
                team.getId());
    }

    @EventListener
    public void handleTeamDeleteEvent(TeamDeleteEvent teamDeleteEvent) {
        multiMessageEvent(teamDeleteEvent.getMembers(),
                "팀원 모집글 <" + teamDeleteEvent.getTeam().getTitle() + ">이 삭제되었습니다",
                null);
    }

    @EventListener
    public void handleTeamApplyEvent(TeamApplyEvent teamApplyEvent) {
        Member member = teamApplyEvent.getMember();
        Team team = teamApplyEvent.getTeam();
        singleMessageEvent(member,
                "팀원 모집글 <" + team.getTitle() + ">에 " + member.getNickname() + "님이 지원하였습니다",
                team.getId());
    }

    @EventListener
    public void handleTeamLeaderApproveEvent(TeamLeaderApproveEvent teamLeaderApproveEvent) {
        Team team = teamLeaderApproveEvent.getTeam();
        multiMessageEvent(teamLeaderApproveEvent.getMembers(),
                "팀원 모집글 <" + team.getTitle() + ">에 팀원으로 승인되었습니다",
                team.getId());
    }

    @EventListener
    public void handleTeamCloseEvent(TeamCloseEvent teamCloseEvent) {
        Team team = teamCloseEvent.getTeam();
        multiMessageEvent(teamCloseEvent.getMembers(),
                "팀원 모집글 <" + team.getTitle() + ">이 팀 모집을 마감하였습니다",
                team.getId());
    }

    @EventListener
    public void handleTeamInviteEvent(TeamInviteEvent teamInviteEvent){
        Member member = teamInviteEvent.getMember();
        singleMessageEvent(member,
                teamInviteEvent.getLeaderNickname() + "님이" + teamInviteEvent.getTitle()
        + "에 팀원으로 요청 하셨습니다.", teamInviteEvent.getTeam_id());
    }

    private void multiMessageEvent(List<Member> members, String body, Long teamId) {
        if (!members.isEmpty()) {
            List<String> tokenList = members.stream()
                    .filter(Member::getUserAlarmOn) // userAlarmOn이 false이면 건너뜀
                    .map(Member::getFcmToken)
                    .filter(fcmToken -> !fcmToken.isBlank()) // 로그이웃한 사용자 제외
                    .collect(Collectors.toList());
            notificationService.sendByTokenList(tokenList, "동문개발자 커뮤니티(Alumni)", body, "1", teamId);
        }
    }

    private void singleMessageEvent(Member member, String body, Long teamId) {
        String fcmToken = member.getFcmToken();
        if (!fcmToken.isBlank() && member.getUserAlarmOn()) { // 로그아웃하면 fcmToken black 됨 + 알람이 on인 경우만 실행
            notificationService.sendByToken(fcmToken, "동문개발자 커뮤니티(Alumni)", body, "1", teamId);
        }
    }

}
