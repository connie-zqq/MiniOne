package edu.northeastern.minione.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.entity.Moment;

/**
 * Create the interface MomentRepository.
 * The repository allows us to access the information stored in the database.
 */
@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {

    /**
     * Find out all the moments that indexed with the input space id
     * and store the info in the input pageable variable.
     * In that case the method name below creates property traversal x.space.id
     * ("Property Expression")
     *
     * @param id The space id of the moment
     * @param pageable A Pageable variable that stores the result
     * @return Page return a pageable variable contains all the moments that indexed with the input space id
     */
    Page<Moment> findAllBySpaceId(Long id, Pageable pageable);

    /**
     * Find out all the moments that indexed with the input space id and store the info in a list.
     *
     * @param id The space id of the moment that is looking for
     * @return List RETURN a list contains all the moments that indexed with the input space id
     */
    List<Moment> findAllBySpaceId(Long id);
}
