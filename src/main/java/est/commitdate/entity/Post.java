package est.commitdate.entity;




import est.commitdate.dto.post.PostUpdateDto;
import lombok.*;


import est.commitdate.dto.post.PostDto;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "Post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Post Set status = 0 WHERE post_id = ?")
@SQLRestriction("status = 1")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id" , nullable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "status", nullable = false )
    private int status = 1;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();


    @OneToMany(mappedBy = "post")
    private List<Comment> comments;


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
//        this.likeCount = dto.getLikeCount();
        this.updatedAt = dto.getUpdatedAt();
    }

    public static Post testTransformEntity(PostDto dto, Board board, Member member) {
        Post post = new Post();
        post.board = board;
        post.member = member;
        post.title = dto.getTitle();
        post.text = dto.getText();
        post.description = dto.getDescription();
        return post;
    }

//     동적으로 likeCount 업데이트
    public Integer updateLikeCount() {
        return likes.size();
    }


    public void delete() {
        this.status = 0;
    }

    public void restore() {
        this.status = 1;
    }

    public Integer commentCount() {
        return this.comments.size();
    }




}

