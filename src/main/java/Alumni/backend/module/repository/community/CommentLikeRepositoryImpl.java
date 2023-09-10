package Alumni.backend.module.repository.community;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.community.QCommentLike.commentLike;
@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Tuple> countCommentLikesByCommentId() {
        return jpaQueryFactory
                .select(commentLike.comment.id, commentLike.count())
                .from(commentLike)
                .groupBy(commentLike.comment.id)
                .fetch();
    }
}

