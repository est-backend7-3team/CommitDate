package est.commitdate.dummy;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "username", length = 30, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "role", length = 10, nullable = false)
    private String role;

    @Column(name = "profile_image", length = 300)
    private String profileImage;

    @Column(name = "introduce", length = 500)
    private String introduce;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

}