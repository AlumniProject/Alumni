package Alumni.backend.module.repository.community.comment;

import Alumni.backend.module.domain.community.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByPostIdAndMemberFetchJoin(Long postId);

    Comment findByIdAndMemberFetchJoin(Long commentId);
}
