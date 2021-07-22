package edu.northeastern.minione.service;

import java.util.List;
import java.util.Optional;

import edu.northeastern.minione.model.Space;

public interface MomentService {
    List<Space> findAll();
    Optional<Space > findById(int id);
    Space create(Space space);
    Space edit(Space space);
    void deleteById(int id);
}
