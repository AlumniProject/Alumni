package Alumni.backend.module.repository.community.comment;

import Alumni.backend.module.domain.community.Comment;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.QImage.image;
import static Alumni.backend.module.domain.community.QComment.comment;
import static Alumni.backend.module.domain.community.QPostLike.postLike;
import static Alumni.backend.module.domain.registration.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findByPostIdFetchJoinMemberAndImage(Long postId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member).fetchJoin()
                .leftJoin(member.profileImage, image).fetchJoin()
                .where(comment.post.id.eq(postId))
                .fetch();
    }

    @Override
    public List<Comment> findByTeamIdFetchJoinMemberAndImage(Long teamId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.member, member).fetchJoin()
                .leftJoin(member.profileImage, image).fetchJoin()
                .where(comment.team.id.eq(teamId))
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

    @Override
    public List<Tuple> countCommentsByPostId() {
        return jpaQueryFactory
                .select(comment.post.id, comment.count())
                .from(comment)
                .groupBy(comment.post.id)
                .fetch();
    }
}
