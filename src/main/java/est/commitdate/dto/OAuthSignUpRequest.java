package est.commitdate.dto;

import lombok.*;
import est.commitdate.entity.Member;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Getter
public class OAuthSignUpRequest {
    private String email;

    public Member toEntity(MemberAdditionalInfo additionalInfo, String provider) {
        return Member.of(this, additionalInfo, provider);
    }

}