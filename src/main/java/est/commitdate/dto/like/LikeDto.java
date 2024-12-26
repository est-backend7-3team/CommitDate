package est.commitdate.dto.like;


import est.commitdate.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {

    private Long likeId; //좋아요생성 번호
    private String MemberId; //좋아요 누른 사람
    private String receiveName; //포스트를 쓴 사람.
    private String postTitle; //포스트 아이디 (바로 보러갈 수 있게)
    private String timestamp;
    private Integer matchingResult;
    private Long postId;
// 타임스탬프찍힌 시간.

    public static LikeDto from(Like like){
        return LikeDto.builder()
                .likeId(like.getLikeId())
                .receiveName(like.getPost().getMember().getNickname())
                .postTitle(like.getPost().getTitle())
                .matchingResult(like.getMatchingResult())
                .timestamp(like.getLikeDate().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")))
                .postId(like.getPost().getPostId())
                .build();
    }
}


