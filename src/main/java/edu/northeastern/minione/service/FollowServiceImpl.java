package edu.northeastern.minione.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.minione.model.Follow;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;
import edu.northeastern.minione.repository.FollowRepository;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    /**
     * Add the user to follow the space.
     *
     * @param user
     * @param space
     * @return
     */
    @Override
    public Follow addFollow(User user, Space space) {
        Follow follow = new Follow(user, space);
        return this.followRepository.save(follow);
    }

    @Override
    public Follow findFollowBySpaceAndUser(User user, Space space) {
        return null;
    }

    /**
     * Find all existed following relationship by space Id.
     *
     * @param spaceId
     * @return
     */
    @Override
    public List<Follow> findAllFollowsBySpaceId(Long spaceId) {
        return this.followRepository.findAllByFollowedSpace_Id(spaceId);
    }

    /**
     * Find all spaces followed by the specific user.
     *
     * @param user
     * @return
     */
    @Override
    public List<Space> findAllSpacesFollowedByUser(User user) {
        List<Space> followedSpaces = new ArrayList<>();
        for (Follow follow : user.getFollows()) {
            followedSpaces.add(follow.getFollowedSpace());
        }
        return followedSpaces;
    }

    /**
     * Find all followers who is following the specific space.
     *
     * @param space
     * @return
     */
    @Override
    public List<User> findAllFollowersbySpace(Space space) {
        List<User> followers = new ArrayList<>();
        for (Follow follow : space.getFollows()) {
            followers.add(follow.getFollower());
        }
        return followers;
    }


}
