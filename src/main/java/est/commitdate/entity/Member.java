package est.commitdate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String username;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 10)
    private String role;

    @Column(name = "profile_image", length = 300)
    private String profileImage;

    @Column(length = 500)
    private String introduce;

    @Column(length = 20)
    private String comment;

    @Column(name = "created_at" , nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at" , nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private int status;

    @Column(nullable = true, length = 20)
    private String provider;

    @Column(nullable = false)
    private boolean additionalInfoCompleted = false;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    public Member(String email,
                  String provider,
                  String role,
                  String username,
                  String nickname,
                  String phoneNumber,
                  boolean additionalInfoCompleted,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt, int status) {
        this.email = email;
        this.provider = provider;
        this.role = role;
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.additionalInfoCompleted = additionalInfoCompleted;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = 1;
    }

    public void updateAdditionalInfo(String nickname, String phoneNumber, String username) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.additionalInfoCompleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    // 다음의 정보들은 회원가입 시 기본값으로 적용시킴
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = 1;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
  
    @Builder
    public Member(String password, String email, String nickname, String username, String phoneNumber, String role, String profileImage, String introduce, String comment, LocalDateTime createdAt, LocalDateTime updatedAt, int status) {

        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.profileImage = profileImage;
        this.introduce = introduce;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        
    }

}
