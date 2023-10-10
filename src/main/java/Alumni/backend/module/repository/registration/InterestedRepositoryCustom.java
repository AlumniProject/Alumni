package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.Member;

import java.util.List;
import java.util.Map;

public interface InterestedRepositoryCustom {
    Map<Member, List<String>> findInterestedMemberId();
}
