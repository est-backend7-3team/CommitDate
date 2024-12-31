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
public class PostUpdateDto {
    private Long postId;
    private Integer boardId;
    private String title;
    private String sourceCode;
    private String text;
    private String description;
    private String author;

//    private Integer likeCount;
//    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int status;

    public static PostUpdateDto from(Post post) {
        return PostUpdateDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .sourceCode(post.getSourceCode())
                .description(post.getDescription())
                .author(post.getMember().getUsername())
                .updatedAt(post.getUpdatedAt())
                .boardId(post.getBoard().getBoardId())
                .build();
    }

}