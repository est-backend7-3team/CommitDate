package est.commitdate.dto;



import est.commitdate.entity.Board;
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
    private String author;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int status;

    public static PostDto from(Post post) {
        PostDto postDto = new PostDto();
        postDto.postId = post.getPostId();
        postDto.boardId = post.getBoard().getBoardId();
        postDto.title = post.getTitle();
        postDto.text = post.getText();
        postDto.author = post.getMember().getUsername();
        postDto.likeCount = post.getLikeCount();
        postDto.createdAt = post.getCreatedAt();
        postDto.updatedAt = post.getUpdatedAt();
        postDto.status = post.getStatus();
        return postDto;
    }

}
