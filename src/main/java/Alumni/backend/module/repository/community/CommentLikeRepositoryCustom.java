package Alumni.backend.module.repository.community;

import com.querydsl.core.Tuple;

import java.util.List;

public interface CommentLikeRepositoryCustom {
    List<Tuple> countCommentLikesByCommentId();
}
