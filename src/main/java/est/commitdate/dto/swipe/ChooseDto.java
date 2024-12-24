package est.commitdate.dto.swipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChooseDto {

    // Choose 에서만 쓰는 Dto
    private String userId;
    private String userName;
    private String comment;
    private String profileImageURL;
    private String timestamp;

}

