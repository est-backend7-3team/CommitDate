package est.commitdate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberAdditionalInfo {

    private String nickname;
    private String phoneNumber;
    private String username;
    private String email; // -> oauth
    private String provider; // -> oautth d


}