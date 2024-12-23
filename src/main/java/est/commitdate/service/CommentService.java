package est.commitdate.service;


import est.commitdate.dto.comment.CommentUpdateDto;
import est.commitdate.entity.Comment;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.exception.CommentNotFoundException;
import est.commitdate.exception.PostNotFoundException;
import est.commitdate.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment save(Post post, Member member, String content) {
        Comment newComment = Comment.of(post, member, content);
        commentRepository.save(newComment);
        return newComment;
    }
    public void update(CommentUpdateDto commentUpdateDto) {
        Comment findComment = getCommentById(commentUpdateDto.getId());
        findComment.update(commentUpdateDto);
    }

    public void delete(Long id) {
         Comment findComment = getCommentById(id);
         findComment.delete();
    }
    public void restore(Long id) {
        Comment findComment = getDeleteCommentById(id);
        findComment.restore();
    }

    public Comment getCommentByPost(Post post) {
        return commentRepository.findByPost(post)
                .orElseThrow(
                        ()-> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다.")
                );
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findByCommentId(id)
                .orElseThrow(
                        ()->new CommentNotFoundException("해당 댓글을 찾을 수 없습니다.")
                );
    }

    public Comment getDeleteCommentById(Long id) {
        return commentRepository.findDeleteById(id)
                .orElseThrow(
                        ()-> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다.")
                );
    }


}
