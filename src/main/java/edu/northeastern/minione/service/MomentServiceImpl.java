package edu.northeastern.minione.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.northeastern.minione.entity.AmazonImage;
import edu.northeastern.minione.entity.Moment;
import edu.northeastern.minione.entity.Space;
import edu.northeastern.minione.repository.FollowRepository;
import edu.northeastern.minione.repository.MomentRepository;
import edu.northeastern.minione.repository.SpaceRepository;

/**
 * This is a class that implements the moment service.
 */
@Service
public class MomentServiceImpl implements MomentService {

    // allow
    @Autowired
    SpaceRepository spaceRepository;

    @Autowired
    MomentRepository momentRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    AmazonS3ImageService amazonS3ImageService;

    /**
     * Find all spaces in the database.
     *
     * @return the list of the Space objects
     */
    @Override
    public List<Space> findAllSpaces() {
        return this.spaceRepository.findAll();
    }

    /**
     * Find all spaces in the database.
     *
     * @param pageable the Pageable object that contains the pagination information
     * @return the list of the Space objects
     */
    @Override
    public Page<Space> findAllSpaces(Pageable pageable) {
        return this.spaceRepository.findAll(pageable);
    }

    /**
     * Find the space by id.
     *
     * @param id the space id
     * @return the Space object
     */
    @Override
    public Space findSpaceById(Long id) {
        return this.spaceRepository.findById(id).orElse(null);
    }

    /**
     * Create a new space in the database.
     *
     * @param space the Space object
     */
    @Override
    public void createSpace(Space space) {
        this.spaceRepository.save(space);
    }

    /**
     * Edit the space in the database.
     *
     * @param space the Space object
     * @return the edited space
     */
    @Override
    public Space editSpace(Space space) {
        return this.spaceRepository.save(space);
    }

    /**
     * Delete the space by id.
     *
     * @param id the space id
     */
    @Override
    public void deleteSpaceById(Long id) {
        this.spaceRepository.deleteById(id);
    }

    /**
     * Set baby profile image signed url for the space.
     *
     * @param space the space Object
     * @return the baby profile image's signed url
     */
    @Override
    public String getBabyProfileImageSignedUrl(Space space) {
        return amazonS3ImageService.
                getGeneratePresignedUrl(space.getBabyImageObjectKey()).toString();
    }

    /**
     * Set signed url for each photo in a photo list.
     *
     * @param photos
     */
    @Override
    public void setEachPhotoSignedUrl(List<AmazonImage> photos) {
        photos.stream().filter(photo -> photo.getObjectKey() != null).forEach(photo -> photo.setSignedUrl(amazonS3ImageService.getGeneratePresignedUrl(photo.getObjectKey()).toString()));
    }

    /**
     * Find all moments in the database.
     *
     * @return the list of the Moment objects
     */
    @Override
    public List<Moment> findAllMoments() {
        return this.momentRepository.findAll();
    }

    /**
     * Find all moments in the database with pagination.
     *
     * @param pageable the Pageable object that contains the pagination information
     * @return the list of the Moment objects
     */
    @Override
    public Page<Moment> findAllMoments(Pageable pageable) {
        return this.momentRepository.findAll(pageable);
    }

    /**
     * Find all moments by space id with pagination.
     *
     * @param spaceId  the space id
     * @param pageable the Pageable object that contains the pagination information
     * @return the list of the Moment objects
     */
    @Override
    public Page<Moment> findAllMomentsBySpaceId(Long spaceId, Pageable pageable) {
        return this.momentRepository.findAllBySpaceId(spaceId, pageable);
    }

    /**
     * Find all moments by space id.
     *
     * @param spaceId the space id
     * @return the list of the Moment objects
     */
    @Override
    public List<Moment> findAllMomentsBySpaceId(Long spaceId) {
        return this.momentRepository.findAllBySpaceId(spaceId);
    }

    /**
     * Find the moment by id.
     *
     * @param id the moment id
     * @return the Moment object
     */
    @Override
    public Moment findMomentById(Long id) {
        return this.momentRepository.findById(id).orElse(null);
    }

    /**
     * Create a new moment in the database.
     *
     * @param moment the Moment object
     */
    @Override
    public void createMoment(Moment moment) {
        this.momentRepository.save(moment);
    }

    /**
     * Edit the moment in the database.
     *
     * @param moment the Space object
     * @return the edited moment
     */
    @Override
    public void editMoment(Moment moment) {
        this.momentRepository.save(moment);
    }

    /**
     * Delete the moment by id.
     *
     * @param id the moment id
     */
    @Override
    public void deleteMomentById(Long id) {
        this.momentRepository.deleteById(id);
    }

    /**
     * Find all photos by the space id.
     *
     * @param spaceId the space Id
     * @return the list of the AmazonImage objects
     */
    @Override
    public List<AmazonImage> findAllPhotosBySpaceId(Long spaceId) {
        List<Moment> moments = this.findAllMomentsBySpaceId(spaceId);
        List<AmazonImage> photos = new ArrayList<>();
        for (int i = moments.size() - 1; i >= 0; i--) {
            if (!moments.get(i).getPhotos().isEmpty())
                photos.addAll(moments.get(i).getPhotos());
        }
        return photos;
    }

    /**
     * Use Stream Filter to get a stream consisting of the moments that have photos, then set signed url for each
     * moment of this stream.
     *
     * @param moments a list of moments with pagination
     */
    @Override
    public void setEachMomentPhotoSignedURL(Page<Moment> moments) {
        // Filter out only moment's photos is not empty, then set the photo's signed url.
        moments.stream().filter(moment -> moment.getPhotos() != null).forEach(moment -> moment.getPhotos()
                .forEach(photo -> photo.setSignedUrl(amazonS3ImageService.getGeneratePresignedUrl(photo.getObjectKey())
                        .toString())));
    }

    /**
     * Use Stream Filter to get a stream consisting of the moments that have the author photo, then set signed url for
     * each moment of this stream.
     *
     * @param moments a list of moments with pagination
     */
    @Override
    public void setEachMomentAuthorPhotoSignedURL(Page<Moment> moments) {
        moments.stream().filter(moment -> moment.getAuthor().getUserImageObjectKey() != null)
                .forEach(moment -> moment.getAuthor().setUserImageSignedUrl(amazonS3ImageService
                        .getGeneratePresignedUrl(moment.getAuthor().getUserImageObjectKey()).toString()));
    }

}
