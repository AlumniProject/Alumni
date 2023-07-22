package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Comment;
import Alumni.backend.module.domain.QComment;
import Alumni.backend.module.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.QComment.*;
import static Alumni.backend.module.domain.QMember.*;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCutom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findByPostIdAndMemberFetchJoin(Long postId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member).fetchJoin()
                .where(comment.post.id.eq(postId))
                .fetch();
    }
}
