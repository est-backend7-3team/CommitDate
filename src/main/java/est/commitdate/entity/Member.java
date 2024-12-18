package est.commitdate.entity;

import est.commitdate.dto.MemberAdditionalInfo;
import est.commitdate.dto.MemberSignUpRequest;
import est.commitdate.dto.OAuthSignUpRequest;
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

//    @Column(nullable = false)
//    private boolean additionalInfoCompleted = false;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

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
  

    // DTO를 통해서 생성할 생성자
    //form 로그인
    public static Member of(MemberSignUpRequest signUpRequest) {
        Member member = new Member();

        member.email = signUpRequest.getEmail();
        member.password = signUpRequest.getPassword();
        member.nickname = signUpRequest.getNickname();
        member.username = signUpRequest.getUsername();
        member.phoneNumber = signUpRequest.getPhoneNumber();
        member.role = "member";
//        member.additionalInfoCompleted = true;
        member.createdAt = LocalDateTime.now();
        member.updatedAt = LocalDateTime.now();
        member.status = 1;

        return member;
    }


    //oauth 로그인
    public static Member of( MemberAdditionalInfo memberAdditionalInfo) {
        Member member = new Member();
        member.email = memberAdditionalInfo.getEmail();
        member.nickname = memberAdditionalInfo.getNickname();
        member.username = memberAdditionalInfo.getUsername();
        member.phoneNumber = memberAdditionalInfo.getPhoneNumber();
        member.role = "member";
        member.provider = member.getProvider();
//        member.additionalInfoCompleted = true;
        member.createdAt = LocalDateTime.now();
        member.updatedAt = LocalDateTime.now();
        member.status = 1;

        return member;
    }

    // 추가정보 업데이트 메서드
//    public void updateAdditionalInfo(String nickname, String phoneNumber, String username) {
//        this.nickname = nickname;
//        this.phoneNumber = phoneNumber;
//        this.username = username;
//        this.additionalInfoCompleted = true;
//        this.updatedAt = LocalDateTime.now();
//    }

}
