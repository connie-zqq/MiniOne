package edu.northeastern.minione.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.repository.MomentRepository;
import edu.northeastern.minione.repository.SpaceRepository;

public class MomentServiceImpl implements MomentService {

    @Autowired
    SpaceRepository spaceRepository;

    @Autowired
    MomentRepository momentRepository;

    @Override
    public List<Space> findAllSpaces() {
        return this.spaceRepository.findAll();
    }

    @Override
    public Optional<Space> findSpaceById(Long id) {
        return this.spaceRepository.findById(id);
    }

    @Override
    public Space createSpace(Space space) {
        return this.spaceRepository.save(space);
    }

    @Override
    public Space editSpace(Space space) {
        return this.spaceRepository.save(space);
    }

    @Override
    public void deleteSpaceById(Long id) {
        this.spaceRepository.deleteById(id);
    }

    @Override
    public List<Moment> findAllMoments() {
        return this.momentRepository.findAll();
    }

    @Override
    public Optional<Moment> findMomentById(Long id) {
        return this.momentRepository.findById(id);
    }

    @Override
    public Moment createMoment(Moment moment) {
        return this.momentRepository.save(moment);
    }

    @Override
    public Moment editMoment(Moment moment) {
        return this.momentRepository.save(moment);
    }

    @Override
    public void deleteMomentById(Long id) {
        this.momentRepository.deleteById(id);
    }
}
