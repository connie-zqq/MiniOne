package edu.northeastern.minione.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.northeastern.minione.model.AmazonImage;
import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.Space;

/**
 * This is the MomentService interface.
 */
public interface MomentService {

    List<Space> findAllSpaces();

    Page<Space> findAllSpaces(Pageable pageable);

    Space findSpaceById(Long id);

    void createSpace(Space space);

    Space editSpace(Space space);

    void deleteSpaceById(Long id);

    String getBabyProfileImageSignedUrl(Space space);

    void setEachPhotoSignedUrl(List<AmazonImage> photos);

    List<Moment> findAllMoments();

    Page<Moment> findAllMoments(Pageable pageable);

    List<Moment> findAllMomentsBySpaceId(Long spaceId);

    Page<Moment> findAllMomentsBySpaceId(Long spaceId, Pageable pageable);

    Moment findMomentById(Long id);

    void createMoment(Moment moment);

    void editMoment(Moment moment);

    void deleteMomentById(Long id);

    List<AmazonImage> findAllPhotosBySpaceId(Long spaceId);

    void setEachMomentPhotoSignedURL(Page<Moment> moments);

    void setEachMomentAuthorPhotoSignedURL(Page<Moment> moments);
}
