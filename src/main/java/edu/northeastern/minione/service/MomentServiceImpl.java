package edu.northeastern.minione.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.repository.FollowRepository;
import edu.northeastern.minione.repository.MomentRepository;
import edu.northeastern.minione.repository.SpaceRepository;

@Service
public class MomentServiceImpl implements MomentService {

    @Autowired
    SpaceRepository spaceRepository;

    @Autowired
    MomentRepository momentRepository;

    @Autowired
    FollowRepository followRepository;

    @Override
    public List<Space> findAllSpaces() {
        return this.spaceRepository.findAll();
    }

    @Override
    public Page<Space> findAllSpaces(Pageable pageable) {
        return this.spaceRepository.findAll(pageable);
    }

    @Override
    public Space findSpaceById(Long id) {
        return this.spaceRepository.findById(id).orElse(null);
    }

    @Override
    public void createSpace(Space space) {
        this.spaceRepository.save(space);
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
    public Page<Moment> findAllMoments(Pageable pageable) {
        return this.momentRepository.findAll(pageable);
    }

    @Override
    public Page<Moment> findAllMomentsBySpaceId(Long spaceId, Pageable pageable) {
        return this.momentRepository.findAllBySpaceId(spaceId, pageable);
    }

    @Override
    public Moment findMomentById(Long id) {
        return this.momentRepository.findById(id).orElse(null);
    }

    @Override
    public void createMoment(Moment moment) {
        this.momentRepository.save(moment);
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
