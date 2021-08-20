package edu.northeastern.minione.service;

import java.util.List;

import edu.northeastern.minione.model.Follow;
import edu.northeastern.minione.model.Invitation;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;

/**
 * This is the FollowService interface.
 */
public interface FollowService {

    void addFollow(User user, Space space);

    void addFollows(User user, List<Space> spaces);

    List<Follow> findAllFollowsBySpaceId(Long spaceId);

    void deleteFollowById(Long id);

    void unfollowSpace(User user, Space space);

    List<Space> findAllSpacesFollowedByUser(User user);

    List<User> findAllFollowersbySpace(Space space);

    void createInvitation(Space space, String email);

    List<Invitation> findAllInvitationsByEmail(String email);

    List<Invitation> findAllInvitationsBySpaceId(Long spaceId);

    void sendInvitationEmail(String email, String spaceName);

    Boolean containInvitationInThisSpace(Invitation invitation_a, List<Invitation> invitations, Long SpaceId);

    List<Space> findAllInvitedSpacesByEmail(String email);

    void deleteInvitations(List<Invitation> invitations);

    List<Space> mergeFollowedSpacesAndInvitedSpaces(List<Space> followedSpaces, List<Space> invitedSpaces);

    void setEachFollowedSpaceBabyAge(List<Space> followedSpaces);

    void setEachFollowedSpaceIsAdmin(List<Space> followedSpaces, User follower);

    void setEachFollowedSpaceUrl(List<Space> followedSpaces);

    void setEachFollowedSpaceUnfollowUrl(List<Space> followedSpaces, User follower);

}
