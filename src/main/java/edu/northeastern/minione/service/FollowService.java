package edu.northeastern.minione.service;

import java.util.List;

import edu.northeastern.minione.model.Follow;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;


public interface FollowService {


    Follow addFollow(User user, Space space);

    Follow findFollowBySpaceAndUser(User user, Space space);

    List<Follow> findAllFollowsBySpaceId(Long spaceId);

    List<Space> findAllSpacesFollowedByUser(User user);

    List<User> findAllFollowersbySpace(Space space);
}
