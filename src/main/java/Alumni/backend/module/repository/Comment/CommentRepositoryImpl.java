package Alumni.backend.module.repository.Comment;

import Alumni.backend.module.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.QComment.*;
import static Alumni.backend.module.domain.QMember.*;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findByPostIdAndMemberFetchJoin(Long postId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member).fetchJoin()
                .where(comment.post.id.eq(postId))
                .fetch();
    }

    @Override
    public Comment findByIdAndMemberFetchJoin(Long commentId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member).fetchJoin()
                .where(comment.id.eq(commentId))
                .fetchOne();
    }
}
