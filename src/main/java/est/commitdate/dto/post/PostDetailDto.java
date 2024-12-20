package est.commitdate.dto.post;

import est.commitdate.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostDetailDto {
    private Long postId;
    private String title;
    private String description;
    private String text;
    private String author;
    private LocalDateTime createdAt;
    private Integer likeCount;



    public static PostDetailDto from (Post post) {
        return PostDetailDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .text(post.getText())
                .description(post.getDescription())
                .author(post.getMember().getUsername())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
