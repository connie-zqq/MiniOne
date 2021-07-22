package edu.northeastern.minione.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.repository.SpaceRepository;

public class MomentServiceImpl implements MomentService {

    @Autowired
    SpaceRepository spaceRepository;

    @Override
    public List<Space> findAll() {
        return this.spaceRepository.findAll();
    }

    @Override
    public Optional<Space> findById(int id) {
        return this.spaceRepository.findById(id);
    }

    @Override
    public Space create(Space space) {
        return this.spaceRepository.save(space);
    }

    @Override
    public Space edit(Space space) {
        return this.spaceRepository.save(space);
    }

    @Override
    public void deleteById(int id) {
        this.spaceRepository.deleteById(id);
    }
}
