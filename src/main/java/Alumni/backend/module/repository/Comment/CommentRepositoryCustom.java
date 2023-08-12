package Alumni.backend.module.repository.Comment;

import Alumni.backend.module.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByPostIdAndMemberFetchJoin(Long postId);

    Comment findByIdAndMemberFetchJoin(Long commentId);
}
