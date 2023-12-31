package Alumni.backend.module.domain.community;

import Alumni.backend.module.domain.BaseTimeEntity;
import Alumni.backend.module.domain.registration.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @OneToMany(mappedBy = "post")
    private Set<PostTag> postTags = new HashSet<>();

    public static Post createPost(Member member, Board board, String title, String content) {
        Post post = new Post();
        post.member = member;
        post.board = board;
        post.title = title;
        post.content = content;
        return post;
    }

    public void postModify(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
