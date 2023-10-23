package Alumni.backend.module.dto.profile;

import Alumni.backend.module.domain.registration.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmStateDto {

    private Boolean normalAlarmOn;
    private Boolean userAlarmOn;

    public static AlarmStateDto getAlarmStateDto(Member member) {
        return AlarmStateDto.builder()
                .normalAlarmOn(member.getNormalAlarmOn())
                .userAlarmOn(member.getUserAlarmOn())
                .build();
    }
}
