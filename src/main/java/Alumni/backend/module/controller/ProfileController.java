package Alumni.backend.module.controller;

import Alumni.backend.infra.config.CurrentUser;
import Alumni.backend.infra.response.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.profile.*;
import Alumni.backend.module.service.profile.FollowService;
import Alumni.backend.module.service.profile.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ApiDocumentGlobalResponse
@ApiDocumentAuthResponse
@Tag(name = "Profile", description = "사용자 관련 api")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final FollowService followService;
    private final ProfileService profileService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로우 완료" + "<br>팔로우 취소 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팔로우 또는 팔로우 취소", description = "팔로우를 하거나 했던 팔로우를 취소하는 메서드입니다.")
    @Parameter(name = "member_id", description = "팔로우 또는 취소할 id", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/follow/{member_id}")
    public ResponseEntity<? extends BasicResponse> follow(@Schema(hidden = true) @CurrentUser Member currentMember, @PathVariable("member_id") Long memberId){

        String message = followService.follow(currentMember, memberId);

        return ResponseEntity.ok().body(new SingleResponse(message));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "닉네임 수정 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "닉네임 수정", description = "닉네임을 수정하는 메서드")
    @Parameter(name = "member_id", description = "수정할 member id", required = true, example = "1", in = ParameterIn.PATH)
    @PatchMapping("/edit/nickname/{member_id}")
    public ResponseEntity<? extends  BasicResponse> editNickname(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("member_id") Long memberId, @RequestBody @Valid NicknameDto nicknameDto){

        profileService.editNickname(member, memberId, nicknameDto);

        return ResponseEntity.ok().body(new SingleResponse("닉네임 수정 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "한줄소개 수정 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "한줄소개 수정", description = "한줄소개를 수정하는 메서드")
    @Parameter(name = "member_id", description = "수정할 member id", required = true, example = "1", in = ParameterIn.PATH)
    @PatchMapping("/edit/introduction/{member_id}")
    public ResponseEntity<? extends BasicResponse> editIntroduction(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("member_id") Long memberId,
                                                                    @RequestBody @Valid IntroductionDto introductionDto){

        profileService.editIntroduction(member, memberId, introductionDto.getIntroduction());

        return ResponseEntity.ok().body(new SingleResponse("한줄소개 수정 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 게시물 조회 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "프로필 게시물 조회", description = "프로필에서 그 사람이 작성한 게시물들을 볼 수 있는 메서드 입니다.")
    @Parameter(name = "member_id", description = "조회하고 싶은 member_id", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/post/{member_id}")
    public ResponseEntity< ? extends BasicResponse> profilePosts(@Schema(hidden = true)  @CurrentUser Member currentMember, @PathVariable("member_id") Long memberId){

        ProfilePostsResponseDto profilePostsResponse = profileService.profilePosts(currentMember, memberId);

        return ResponseEntity.ok().body(new DataResponse<>(profilePostsResponse, "프로필 게시물 조회 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "페이스북 링크 추가 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "페이스북 링크 추가", description = "페이스북 링크 추가 메서드")
    @Parameter(name = "member_id", description = "수정할 member id", required = true, example = "1", in = ParameterIn.PATH)
    @PatchMapping("/edit/facebook/{member_id}")
    public ResponseEntity<? extends  BasicResponse> editFacebook(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("member_id") Long memberId, @RequestBody @Valid LinkDto linkDto){

        profileService.editLink(member, memberId, linkDto.getLink());

        return ResponseEntity.ok().body(new SingleResponse("페이스북 링크 추가 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인스타그램 링크 추가 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "인스타그램 링크 추가", description = "인스타그램 링크 추가 메서드")
    @Parameter(name = "member_id", description = "수정할 member id", required = true, example = "1", in = ParameterIn.PATH)
    @PatchMapping("/edit/instagram/{member_id}")
    public ResponseEntity<? extends  BasicResponse> editInstagram(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("member_id") Long memberId, @RequestBody @Valid LinkDto linkDto){

        profileService.editLink(member, memberId, linkDto.getLink());

        return ResponseEntity.ok().body(new SingleResponse("인스타그램 링크 추가 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "깃허브 링크 추가 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "깃허브 링크 추가", description = "깃허브 링크 추가 메서드")
    @Parameter(name = "member_id", description = "수정할 member id", required = true, example = "1", in = ParameterIn.PATH)
    @PatchMapping("/edit/github/{member_id}")
    public ResponseEntity<? extends  BasicResponse> editGithub(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("member_id") Long memberId, @RequestBody @Valid LinkDto linkDto){

        profileService.editLink(member, memberId, linkDto.getLink());

        return ResponseEntity.ok().body(new SingleResponse("깃허브 링크 추가 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스킬 추가 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 스킬" + "<br>존재하지 않는 회원"+"<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "스킬 추가", description = "스킬 추가 메서드")
    @Parameter(name = "member_id", description = "스킬을 추가 할 member id", required = true, example = "1", in = ParameterIn.PATH)
    @PutMapping("/edit/skill/{member_id}")
    public ResponseEntity<? extends BasicResponse> editSkill(@Schema(hidden = true) @CurrentUser Member currentMember, @RequestBody @Valid SkillRequestDto skillRequestDto, @PathVariable("member_id") Long memberId){
        profileService.editSkill(currentMember, memberId, skillRequestDto);

        return ResponseEntity.ok().body(new SingleResponse("스킬 추가 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 회원"+"<br>HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "프로필 조회", description = "프로필 조회 완료")
    @Parameter(name = "member_id", description = "프로필을 조회 할 member id", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/home/{member_id}")
    public ResponseEntity<? extends BasicResponse> profileHome(@Schema(hidden = true) @CurrentUser Member currentMember, @PathVariable("member_id") Long memberId){

        ProfileHomeResponseDto profileHomeResponseDto = profileService.profileHome(currentMember, memberId);

        return ResponseEntity.ok().body(new DataResponse<>(profileHomeResponseDto,"프로필 조회 완료"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 on/off", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "HTTP_REQUEST_ERROR" + "<br>UNEXPECTED_ERROR"
                    + "<br>VALID_ERROR" + "<br>HTTP_REQUEST_ERROR" + "<br>Bad Request" + "<br>다시 로그인해주세요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "알림 on/off 설정", description = "알림 on/off 완료")
    @PostMapping("/alarm")
    public ResponseEntity<? extends BasicResponse> alarmOnOff(@Schema(hidden = true) @CurrentUser Member member) {
        profileService.alarmOnOff(member);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }
}
