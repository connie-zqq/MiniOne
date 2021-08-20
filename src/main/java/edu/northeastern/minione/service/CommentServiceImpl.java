package edu.northeastern.minione.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.northeastern.minione.entity.Comment;
import edu.northeastern.minione.entity.Like;
import edu.northeastern.minione.entity.Moment;
import edu.northeastern.minione.entity.User;
import edu.northeastern.minione.repository.CommentRepository;
import edu.northeastern.minione.repository.LikeRepository;

/**
 * This is a class that implements the comment service.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    LikeRepository likeRepository;

    /**
     * Find all comments by the moment id
     *
     * @param momentId the moment id
     * @return the list of comment objects
     */
    @Override
    public List<Comment> findAllCommentsByMomentId(Long momentId) {
        return this.commentRepository.findAllByCommentedMomentId(momentId);
    }

    /**
     * Find all comments with the pagination.
     *
     * @param pageable the Pageable object that contains the pagination information
     * @return the pages of the comment objects
     */
    @Override
    public Page<Comment> findAllComments(Pageable pageable) {
        return this.commentRepository.findAll(pageable);
    }

    /**
     * Find all comments by the moment id with pagination
     *
     * @param momentId the moment id
     * @param pageable the Pageable object that contains the pagination information
     * @return the list of comment objects
     */
    @Override
    public Page<Comment> findAllCommentsByMomentId(Long momentId, Pageable pageable) {
        return this.commentRepository.findAllByCommentedMomentId(momentId, pageable);
    }

    /**
     * Find comment by comment id.
     *
     * @param commentId the comment id
     * @return the Comment object
     */
    @Override
    public Comment findCommentById(Long commentId) {
        return this.commentRepository.findById(commentId).orElse(null);
    }

    /**
     * Create a comment and then save it in the database.
     *
     * @param comment the Comment object
     */
    @Override
    public void createComment(Comment comment) {
        this.commentRepository.save(comment);
    }

    /**
     * Edit a comment and then save it in the database.
     *
     * @param comment the Comment object
     * @return the edited Comment object
     */
    @Override
    public Comment editComment(Comment comment) {
        return this.commentRepository.save(comment);
    }

    /**
     * Delete the comment by the comment id.
     *
     * @param commentId the comment id
     */
    @Override
    public void deleteCommentById(Long commentId) {
        this.commentRepository.deleteById(commentId);
    }

    /**
     * Delete the comments related to the specific moment.
     *
     * @param moment the Moment object
     */
    @Override
    public void deleteCommentsByMoment(Moment moment) {
        List<Comment> comments = moment.getComments();
        for (Comment comment : comments) {
            this.deleteCommentById(comment.getId());
        }
    }

    /**
     * Save the like in the database.
     *
     * @param like the Like object
     */
    @Override
    public void Like(Like like) {
        this.likeRepository.save(like);
    }

    /**
     * Delete the like in the database.
     *
     * @param id the like id
     */
    @Override
    public void cancelLike(Long id) {
        this.likeRepository.deleteById(id);
    }

    /**
     * Delete the like of the specific moment.
     *
     * @param moment the Moment object
     */
    @Override
    public void deleteLikesByMoment(Moment moment) {
        List<Like> likes = moment.getLikes();
        for (Like like : likes) {
            this.likeRepository.deleteById(like.getId());
        }
    }

    /**
     * Find the like by user and moment.
     *
     * @param user   the User object
     * @param moment the Moment object
     * @return the Like object
     */
    @Override
    public Like findLikeByUserAndMoment(User user, Moment moment) {
        return this.likeRepository.findByFanAndLikedMoment(user, moment);
    }

    /**
     * Set each moment's fans string.
     * The fans are those who like the moment.
     *
     * @param moments the pages of moment
     */
    @Override
    public void setEachMomentFansString(Page<Moment> moments) {
        moments.stream().filter(moment -> !moment.getLikes().isEmpty())
                .forEach(moment -> moment.setMomentFansString(moment.generateMomentFansString(moment.getLikes())));
    }
}
