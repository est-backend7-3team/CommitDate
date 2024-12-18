package est.commitdate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthSignUpRequest {
    private String email;       // OAuth 에서 가져온 이메일
    private String nickname;
    private String phoneNumber;
    private String username;
}