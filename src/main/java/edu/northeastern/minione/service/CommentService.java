package edu.northeastern.minione.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.northeastern.minione.model.Comment;
import edu.northeastern.minione.model.Like;
import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.User;

/**
 * This is the CommentService interface.
 */
public interface CommentService {

    List<Comment> findAllCommentsByMomentId(Long momentId);

    Page<Comment> findAllComments(Pageable pageable);

    Page<Comment> findAllCommentsByMomentId(Long momentId, Pageable pageable);

    Comment findCommentById(Long commentId);

    void createComment(Comment comment);

    Comment editComment(Comment comment);

    void deleteCommentById(Long commentId);

    void deleteCommentsByMoment(Moment moment);

    void Like(Like like);

    void cancelLike(Long id);

    void deleteLikesByMoment(Moment moment);

    Like findLikeByUserAndMoment(User user, Moment moment);

    void setEachMomentFansString(Page<Moment> moments);
}
