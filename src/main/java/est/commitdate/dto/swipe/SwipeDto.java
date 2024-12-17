package est.commitdate.dto.swipe;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwipeDto {

    // Swipe 창에서만 쓰는 Dto
    private Long id;
    private String title;
    private String profileImageURL;
    private String userName;
    private String introduce;
    private Integer likeCount;
    private String comment;
    private String sourceCode;

}
