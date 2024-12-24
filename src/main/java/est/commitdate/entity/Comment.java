package est.commitdate.entity;


import est.commitdate.dto.comment.CommentUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE Comment Set status = 0 WHERE comment_id = ?")
@SQLRestriction("status = 1")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "comment")
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="update_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "status", nullable = false)
    private int status = 1;


    public static Comment of(Post post, Member writer,String content) {
        Comment newComment = new Comment();
        newComment.content = content;
        newComment.post = post;
        newComment.member = writer;
        return newComment;
    }

    public void update(String text) {
        content = text;
        updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.status = 0;
    }

    public void restore() {
        this.status = 1;
    }

}
