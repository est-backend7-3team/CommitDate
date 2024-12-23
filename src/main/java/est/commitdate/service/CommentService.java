package est.commitdate.service;


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

    public Comment getCommentByPost(Post post) {
        return commentRepository.findByPost(post)
                .orElseThrow(
                        ()-> new CommentNotFoundException("해당 게시글을 찾을 수 없습니다.")
                );
    }


}
