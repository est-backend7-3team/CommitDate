package est.commitdate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@SQLDelete(sql = "UPDATE Member Set status = 0 WHERE member_id = ?")
@SQLRestriction("status = 1")
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

    private boolean isTempPassword = false;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    @OneToMany(mappedBy = "member")
    private List<Like> likes;

    @OneToMany(mappedBy = "member")
    private List<Ignore> ignores;

    public Member(String email,
                  String provider,
                  String role,
                  String username,
                  String nickname,
                  String phoneNumber,
                  String password,
                  boolean additionalInfoCompleted,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt, int status) {
        this.email = email;
        this.provider = provider;
        this.role = role;
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.password = password;
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

    public void updateMemberDetails(String email, String encryptedPassword, String nickname, String phoneNumber, String profileImage, String comment, String introduce) {
        this.email = email;
        this.password = encryptedPassword;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.comment = comment;
        this.introduce = introduce;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeTempPassword(String encodedPassword) {
        this.password = encodedPassword;
        this.isTempPassword = true;
    }

    public void clearTempPassword() {
        this.isTempPassword = false;
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

    // 유저가 탈퇴하더라도 작성한 게시글은 삭제하지않음
    public void delete() {
        this.status = 0;
    }

}
