package Alumni.backend.module.repository.community;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.community.QCommentLike.commentLike;
import static Alumni.backend.module.domain.community.QPostLike.postLike;

@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Tuple> countPostLikesByPostId() {
        return jpaQueryFactory
                .select(postLike.post.id, postLike.count())
                .from(postLike)
                .groupBy(postLike.post.id)
                .fetch();
    }
}
