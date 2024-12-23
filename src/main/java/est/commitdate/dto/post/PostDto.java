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
public class PostDto {
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

    public static PostDto from(Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .boardId(post.getBoard().getBoardId())
                .title(post.getTitle())
                .text(post.getText())
                .description(post.getDescription())
//                .author(post.getMember().getUsername())
                .author("11")
                .likeCount(post.updateLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .status(post.getStatus())
                .build();
    }

}
