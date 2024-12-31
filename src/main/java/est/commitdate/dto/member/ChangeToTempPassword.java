package est.commitdate.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeToTempPassword {

    private String currentPassword;
    private String newPassword;

}
