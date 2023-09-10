package Alumni.backend.module.repository.community.comment;

import Alumni.backend.module.domain.community.Comment;
import com.querydsl.core.Tuple;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByPostIdFetchJoinMemberAndImage(Long postId);

    List<Comment> findByTeamIdFetchJoinMemberAndImage(Long teamId);

    Comment findByIdAndMemberFetchJoin(Long commentId);

    List<Tuple> countCommentsByPostId();
}
