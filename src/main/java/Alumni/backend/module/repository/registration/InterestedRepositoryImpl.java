package Alumni.backend.module.repository.registration;

import Alumni.backend.module.domain.registration.Member;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Alumni.backend.module.domain.registration.QInterested.interested;

@RequiredArgsConstructor
public class InterestedRepositoryImpl implements InterestedRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Map<Member, List<String>> findInterestedMemberId() {
        List<Tuple> tuples = jpaQueryFactory
                .select(interested.member, interested.interestField.fieldName)
                .from(interested)
                .fetch();

        return getGroupedResult(tuples);
    }

    private Map<Member, List<String>> getGroupedResult(List<Tuple> tuples) {
        Map<Member, List<String>> resultMap = new HashMap<>();

        for (Tuple tuple : tuples) {
            Member member = tuple.get(interested.member);
            String interestFieldName = tuple.get(interested.interestField.fieldName);

            if (!resultMap.containsKey(member)) {//memberId가 새로운 경우 새로 리스트를 만들어서 추가
                resultMap.put(member, new ArrayList<>());
            }

            resultMap.get(member).add(interestFieldName);//기존에 있는거는 그냥 추가
        }

        return resultMap;
    }
}
