package Alumni.backend.module.domain.community;

import Alumni.backend.module.domain.registration.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static PostLike createPostLike(Post post, Member member){
        PostLike postLike = new PostLike();

        postLike.post = post;
        postLike.member = member;

        return postLike;
    }

    public void setMember(Member member){
        this.member = member;
        member.getPostLikes().add(this);
    }
}
