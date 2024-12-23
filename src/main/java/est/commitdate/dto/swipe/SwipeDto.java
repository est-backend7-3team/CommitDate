package est.commitdate.dto.swipe;


import est.commitdate.entity.Post;
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
    private String comment;
    private String sourceCode;
    private String responseCode;
    private Integer likeCount;
    private Integer isLike;
    private Integer isBlocked;


    public static SwipeDto from(Post post){
        return SwipeDto.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .profileImageURL(post.getMember().getProfileImage())
                .userName(post.getMember().getUsername())
                .introduce(post.getMember().getIntroduce())
                .likeCount(post.updateLikeCount())
                .comment(post.getMember().getComment())
                .sourceCode(post.getText())
                .build();
    }




}


