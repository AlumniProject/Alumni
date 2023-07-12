package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllPosts();
}
