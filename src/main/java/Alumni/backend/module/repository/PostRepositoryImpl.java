package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Post;
import Alumni.backend.module.domain.QImage;
import Alumni.backend.module.domain.QMember;
import Alumni.backend.module.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Alumni.backend.module.domain.QImage.*;
import static Alumni.backend.module.domain.QMember.*;
import static Alumni.backend.module.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Post> findAllPosts() {
        return jpaQueryFactory
                .selectFrom(post)
                .join(post.member, member).fetchJoin()
                .join(member.profileImage, image).fetchJoin()
                .fetch();
    }
}
