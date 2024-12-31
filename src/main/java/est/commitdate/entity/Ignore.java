package est.commitdate.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Post_Ignore")
@SQLRestriction("status = 1")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Ignore Set status = 0 WHERE like_id = ?")
public class Ignore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ignore_id")
    private Long ignoreId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "expiration_at")
    private LocalDateTime expirationDate;

    @Builder
    private Ignore(Member member, Post post, LocalDateTime expirationDate) {
        this.member = member;
        this.post = post;
        this.expirationDate = expirationDate;
    }

    public static Ignore of(Member member, Post post) {
        return Ignore.builder()
                .member(member)
                .post(post)
                .expirationDate(LocalDateTime.now().plusWeeks(1))
                .build();
    }

    public void restore() {
        this.status = 1;
    }

    public void delete() {
        this.status = 0;
    }




}





