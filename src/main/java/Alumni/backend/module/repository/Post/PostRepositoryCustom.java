package Alumni.backend.module.repository.Post;

import Alumni.backend.module.domain.Post;
import Alumni.backend.module.dto.requestDto.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllPosts();

    List<Post> searchPost(PostSearch postSearch);

    Post findByIdFetchJoin(Long postId);

    List<Post> findPopularPosts(List<Long> list);
}
