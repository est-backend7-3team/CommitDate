package est.commitdate.dto.comment;

import est.commitdate.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentUpdateDto {
    private Long id;
    private String content;
    private LocalDateTime update_At;
    private Long postId;

}
