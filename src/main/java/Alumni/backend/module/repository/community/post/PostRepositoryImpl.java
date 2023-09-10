package Alumni.backend.module.repository.community.post;

import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.dto.community.PostSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.QImage.image;
import static Alumni.backend.module.domain.community.QPost.post;
import static Alumni.backend.module.domain.community.QPostLike.postLike;
import static Alumni.backend.module.domain.registration.QMember.member;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findAllPosts() {
        return jpaQueryFactory
            .selectFrom(post)
            .leftJoin(post.member, member).fetchJoin()
            .leftJoin(member.profileImage, image).fetchJoin()
            .fetch();
    }

    @Override
    public List<Post> searchPost(PostSearch postSearch) { // 게시판 ID와 content로 검색
        return jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.member, member).fetchJoin()
                .leftJoin(member.profileImage, image).fetchJoin()
                .where(post.board.id.eq(postSearch.getId()),
                        getTitleContentContains(postSearch.getContent()))
                .fetch();
    }

    @Override
    public Post findByIdFetchJoinMemberAndImage(Long postId) {
        return jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.member, member).fetchJoin()
                .leftJoin(member.profileImage, image).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne();
    }

    @Override
    public List<Post> findPopularPosts() {
        return jpaQueryFactory
                .select(postLike.post)
                .from(postLike)
                .leftJoin(postLike.post, post)
                .groupBy(postLike.post)
                .orderBy(postLike.post.count().desc())
                .fetch();
    }

    private BooleanExpression getTitleContentContains(String titleOrContent) {
        if (titleOrContent != null) {
            return getTitleContains(titleOrContent).or(getContentContains(titleOrContent));
        } else {
            return null;
        }
    }

    private BooleanExpression getTitleContains(String title) {
        return title != null ? post.title.contains(title) : null;
    }

    private BooleanExpression getContentContains(String content) {
        return content != null ? post.content.contains(content) : null;
    }
}
