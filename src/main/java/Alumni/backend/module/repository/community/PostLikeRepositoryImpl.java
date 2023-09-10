package Alumni.backend.module.repository.community;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

import static Alumni.backend.module.domain.community.QPostLike.postLike;
import static Alumni.backend.module.repository.contest.TeamRepositoryImpl.getLongLongHashMap;

@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public HashMap<Long, Long> countPostLikesByPostId() {
        List<Tuple> tuples = jpaQueryFactory
                .select(postLike.post.id, postLike.count())
                .from(postLike)
                .groupBy(postLike.post.id)
                .fetch();

        return getLongLongHashMap(tuples);
    }
}
