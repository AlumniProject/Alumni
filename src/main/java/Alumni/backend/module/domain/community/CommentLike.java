package Alumni.backend.module.domain.community;

import Alumni.backend.module.domain.registration.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static CommentLike createCommentLike(Comment comment, Member member){
        CommentLike commentLike = new CommentLike();

        commentLike.comment = comment;
        commentLike.member = member;

        return commentLike;
    }

    public void setMember(Member member){
        this.member = member;
        member.getCommentLikes().add(this);
    }
}
