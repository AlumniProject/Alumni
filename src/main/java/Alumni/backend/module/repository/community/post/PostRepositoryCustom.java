package Alumni.backend.module.repository.community.post;

import Alumni.backend.module.domain.community.Post;
import Alumni.backend.module.dto.community.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllPosts();

    List<Post> searchPost(PostSearch postSearch);

    Post findByIdFetchJoin(Long postId);

    List<Post> findPopularPosts(List<Long> list);
}
