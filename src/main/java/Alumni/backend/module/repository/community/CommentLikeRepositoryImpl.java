package Alumni.backend.module.repository.community;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

import static Alumni.backend.module.domain.community.QCommentLike.commentLike;
import static Alumni.backend.module.repository.contest.TeamRepositoryImpl.getLongLongHashMap;

@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public HashMap<Long, Long> countCommentLikesByCommentId() {
        List<Tuple> tuples = jpaQueryFactory
                .select(commentLike.comment.id, commentLike.count())
                .from(commentLike)
                .groupBy(commentLike.comment.id)
                .fetch();

        return getLongLongHashMap(tuples);
    }
}

