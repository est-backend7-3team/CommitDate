package est.commitdate.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import java.time.LocalDateTime;



@Entity
@Getter
@Table(name = "Post_Like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE like Set status = 0 WHERE like_id = ?")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "creadted_at")
    private LocalDateTime likeDate;

    @Builder
    public Like(Member member, Post post, LocalDateTime likeDate) {
        this.member = member;
        this.post = post;
        this.likeDate = likeDate;
    }

    public static Like of(Member member, Post post) {
        return Like.builder()
                .member(member)
                .post(post)
                .likeDate(LocalDateTime.now())
                .build();
    }

    public void restore() {
        this.status = 1;
    }

    public void delete() {
        this.status = 0;
    }




}





