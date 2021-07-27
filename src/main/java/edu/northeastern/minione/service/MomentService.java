package edu.northeastern.minione.service;

import java.util.List;
import java.util.Optional;

import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.Space;

public interface MomentService {

    List<Space> findAllSpaces();

    Optional<Space> findSpaceById(Long id);

    Space createSpace(Space space);

    Space editSpace(Space space);

    void deleteSpaceById(Long id);

    List<Moment> findAllMoments();

    Optional<Moment> findMomentById(Long id);

    Moment createMoment(Moment moment);

    Moment editMoment(Moment moment);

    void deleteMomentById(Long id);

}
