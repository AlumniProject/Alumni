package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Post;
import Alumni.backend.module.dto.requestDto.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllPosts();

    List<Post> searchPost(PostSearch postSearch);

    Post findByIdFetchJoin(Long postId);
}
