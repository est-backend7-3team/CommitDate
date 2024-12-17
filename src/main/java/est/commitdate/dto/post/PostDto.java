package est.commitdate.dto.post;



import est.commitdate.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostDto1 {
    private Long postId;
    private Integer boardId;
    private String title;
    private String text;
    private String description;
    private String author;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int status;

    public static PostDto1 from(Post post) {
        return PostDto1.builder()
                .postId(post.getPostId())
                .boardId(post.getBoard().getBoardId())
                .title(post.getTitle())
                .text(post.getText())
                .description(post.getDescription())
//                .author(post.getMember().getUsername())
                .author("작성자1")
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .status(post.getStatus())
                .build();
    }

}
