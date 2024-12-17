package est.commitdate.entity;

import est.commitdate.dto.PostDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;


@Builder//Test 짤 때 필요
@AllArgsConstructor//Test 짤 때 필요
@NoArgsConstructor//Test 짤 때 필요
@Entity
@Getter
@Table(name = "Post")
@SQLDelete(sql = "UPDATE Board Set status = 0 WHERE board_id = ?")
@SQLRestriction("status = 1")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "like_count" , nullable = false)
    private Integer likeCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "status", nullable = false )
    private int status = 1;

    public static Post of(PostDto dto , Board board) {
        Post post = new Post();
        post.board = board;
        post.title = dto.getTitle();
        post.text = dto.getText();
        post.description = dto.getDescription();
        return post;
    }

    public void update(PostDto dto) {
        this.title = dto.getTitle();
        this.text = dto.getText();
        this.description = dto.getDescription();
        this.likeCount = dto.getLikeCount();
        this.updatedAt = dto.getUpdatedAt();
    }

    public void delete() {
        this.status = 0;
    }

    public void restore() {
        this.status = 1;
    }

}
//public class Post {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "post_id")
//    private Long postId;
//
//    @ManyToOne
//    @JoinColumn(name = "board_id", nullable = false)
//    private Board board;
//
//    @Column(name = "title", nullable = false, length = 50)
//    private String title;
//
//    @Column(name = "text", nullable = false)
//    private String text;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;
//
//    @Column(name = "like_count")
//    private Integer likeCount = 0;
//
//    @Column(name = "created_at", nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt = LocalDateTime.now();
//
//    @Column(name = "status", nullable = false)
//    private int status = 1;
//
//    public Post(Board board, String title, String text, Member member, Integer likeCount, LocalDateTime createdAt, LocalDateTime updatedAt, int status) {
//        this.board = board;
//        this.title = title;
//        this.text = text;
//        this.member = member;
//        this.likeCount = likeCount;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.status = status;
//    }
//
//}





