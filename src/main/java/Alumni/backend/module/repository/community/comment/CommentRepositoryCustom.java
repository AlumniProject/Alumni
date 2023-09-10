package Alumni.backend.module.repository.community.comment;

import Alumni.backend.module.domain.community.Comment;

import java.util.HashMap;
import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByPostIdFetchJoinMemberAndImage(Long postId);

    List<Comment> findByTeamIdFetchJoinMemberAndImage(Long teamId);

    Comment findByIdAndMemberFetchJoin(Long commentId);

    HashMap<Long, Long> countCommentsByPostId();
}
