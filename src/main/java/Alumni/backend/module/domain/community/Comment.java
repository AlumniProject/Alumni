package Alumni.backend.module.domain.community;

import Alumni.backend.module.domain.BaseTimeEntity;
import Alumni.backend.module.domain.contest.Team;
import Alumni.backend.module.domain.registration.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>(); // 자식 댓글

    public static Comment createComment(Member member, String content){
        Comment comment = new Comment();

        comment.content = content;
        comment.member = member;
        comment.parent = null;

        return comment;
    }

    public void setPost(Post post){
        this.post = post;
    }

    public void setParent(Comment parent){
        this.parent = parent;
        parent.getChildren().add(this);
    }

    public void setTeam(Team team){
        this.team = team;
    }

    public void modifyComment(String content) {
        this.content = content;
    }
}
