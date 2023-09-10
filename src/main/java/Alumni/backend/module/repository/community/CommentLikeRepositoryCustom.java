package Alumni.backend.module.repository.community;

import java.util.HashMap;

public interface CommentLikeRepositoryCustom {
    HashMap<Long, Long> countCommentLikesByCommentId();
}
