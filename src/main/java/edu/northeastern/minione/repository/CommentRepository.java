package edu.northeastern.minione.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find out all the comments that indexed with the input momentId
     * and store the info in the input pageable variable.
     *
     * @param id       The momentId of the comment
     * @param pageable A variable that stores the result
     * @return Page RETURN a pageable variable contains all the comments that indexed with the input momentId
     */
    Page<Comment> findAllByCommentedMomentId(Long id, Pageable pageable);

    /**
     * Find out all the comments that indexed with the input momentId and store the info in a list.
     *
     * @param id The momentId of the comment that is looking for
     * @return List RETURN a list contains all the comments that indexed with the input momentId
     */
    List<Comment> findAllByCommentedMomentId(Long id);
}
