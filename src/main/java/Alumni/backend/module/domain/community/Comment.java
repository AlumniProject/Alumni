package Alumni.backend.module.domain.community;

import Alumni.backend.module.domain.BaseTimeEntity;
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

    @Column(nullable = false)
    private Integer likeNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();//자식 댓글

    public static Comment createComment(Member member, Post post, String content){
        Comment comment = new Comment();

        comment.content = content;
        comment.likeNum = 0;
        comment.post = post;
        comment.member = member;
        comment.parent = null;

        return comment;
    }

    public void setParent(Comment parent){
        this.parent = parent;
        parent.getChildren().add(this);
    }

    public void modifyComment(String content) {
        this.content = content;
    }
}
