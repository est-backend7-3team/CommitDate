package est.commitdate.dto;

import est.commitdate.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
                .author("작성자1")
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .status(post.getStatus())
                .build();
    }
}
//public class PostDto {
//
//
//    private Long boardId;
//    private String title;
//    private String text;
//    private String userName;
//    private Integer likeCount;
//    private LocalDateTime createdAt = LocalDateTime.now();
//    private LocalDateTime updatedAt = LocalDateTime.now();
//    private int status;
//
//    public static PostDto from(Post post){
//        return new PostDto(
//
//                post.getBoard().getBoardId(),
//                post.getTitle(),
//                post.getText(),
//                post.getMember().getUsername(),
//                post.getLikeCount(),
//                post.getCreatedAt(),
//                post.getUpdatedAt(),
//                post.getStatus()
//
//        );
//    }
//
//}
