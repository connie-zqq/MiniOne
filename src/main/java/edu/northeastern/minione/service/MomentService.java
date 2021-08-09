package edu.northeastern.minione.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.Space;


public interface MomentService {

    List<Space> findAllSpaces();

    Page<Space> findAllSpaces(Pageable pageable);

    Space findSpaceById(Long id);

    void createSpace(Space space);

    Space editSpace(Space space);

    void deleteSpaceById(Long id);

    List<Moment> findAllMoments();

    Page<Moment> findAllMoments(Pageable pageable);

    Page<Moment> findAllMomentsBySpaceId(Long spaceId, Pageable pageable);

    Moment findMomentById(Long id);

    void createMoment(Moment moment);

    Moment editMoment(Moment moment);

    void deleteMomentById(Long id);

}
