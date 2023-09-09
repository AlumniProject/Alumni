package Alumni.backend.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "home Response")
public class HomeResponse<T> extends BasicResponse{
    @Schema(description = "응답 코드", defaultValue = "200")
    private int code; // 응답 코드
    @Schema(description = "응답 메시지")
    private String message; // 응답 메시지
    @Schema(description = "데이터 결과")
    private T profile;

    private T contest;
    private T popularPost;


    public HomeResponse(T profile,T contest,T popularPost) {
        this.profile = profile;
        this.contest = contest;
        this.popularPost = popularPost;
        this.code = 200;
        this.message = "홈 조회 완료";
    }
}
