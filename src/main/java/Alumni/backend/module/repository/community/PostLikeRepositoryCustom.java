package Alumni.backend.module.repository.community;

import java.util.HashMap;

public interface PostLikeRepositoryCustom {

    HashMap<Long, Long> countPostLikesByPostId();
}
