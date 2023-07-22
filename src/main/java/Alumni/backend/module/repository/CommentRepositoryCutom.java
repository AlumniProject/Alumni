package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Comment;

import java.util.List;

public interface CommentRepositoryCutom {

    List<Comment> findByPostIdAndMemberFetchJoin(Long postId);
}
