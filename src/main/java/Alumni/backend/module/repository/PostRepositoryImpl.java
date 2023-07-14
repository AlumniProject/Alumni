package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Post;
import Alumni.backend.module.dto.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static Alumni.backend.module.domain.QImage.*;
import static Alumni.backend.module.domain.QMember.*;
import static Alumni.backend.module.domain.QPost.*;
import static org.springframework.util.StringUtils.hasText;

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
