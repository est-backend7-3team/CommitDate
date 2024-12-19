package est.commitdate.entity;




import est.commitdate.dto.post.PostUpdateDto;
import lombok.*;


import est.commitdate.dto.post.PostDto;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;




@Entity
@Getter
@Table(name = "Post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Post Set status = 0 WHERE post_id = ?")
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


    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "like_count" , nullable = false)
    private Integer likeCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "status", nullable = false )
    private int status = 1;

    @Builder//Test 짤 때 필요
    public static Post of(PostDto dto , Board board, Member member) {
        Post post = new Post();
        post.board = board;
        post.title = dto.getTitle();
        post.text = dto.getText();
        post.description = dto.getDescription();
        post.member = member;
        return post;
    }

    public void update(PostUpdateDto dto) {
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

