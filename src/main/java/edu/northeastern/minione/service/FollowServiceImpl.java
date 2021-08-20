package edu.northeastern.minione.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import edu.northeastern.minione.model.Follow;
import edu.northeastern.minione.model.Invitation;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;
import edu.northeastern.minione.repository.FollowRepository;
import edu.northeastern.minione.repository.InvitationRepository;

/**
 * This is a class that implement the follow service.
 */
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Add the user to follow one space.
     *
     * @param user  the User object
     * @param space the Space object
     */
    @Override
    public void addFollow(User user, Space space) {
        // Check if the follow relationship exists in the database:
        // to make sure the follow relationship is unique
        Follow existedFollow = this.followRepository.findByFollowerAndFollowedSpace(user, space);
        // - if yes, then do not add follow relationship
        if (existedFollow != null) {
            return;
        }
        // - if no, add new follow relationship
        Follow follow = new Follow(user, space);
        this.followRepository.save(follow);
    }

    /**
     * Add the user to follow multiple spaces.
     *
     * @param user   the User object
     * @param spaces the list of spaces
     */
    @Override
    public void addFollows(User user, List<Space> spaces) {
        for (Space space : spaces) {
            this.addFollow(user, space);
        }
    }

    /**
     * Delete the follow by follow id.
     *
     * @param id the follow id
     */
    @Override
    public void deleteFollowById(Long id) {
        this.followRepository.deleteById(id);
    }

    /**
     * The user unfollow the space.
     *
     * @param user  the User object
     * @param space the Space object
     */
    @Override
    public void unfollowSpace(User user, Space space) {
        Follow follow = this.followRepository.findByFollowerAndFollowedSpace(user, space);
        // The owner of the space cannot unfollow the space
        if (!space.getOwner().equals(user)) {
            this.deleteFollowById(follow.getId());
        }
    }

    /**
     * Find all existed following relationship by space Id.
     *
     * @param spaceId the space id
     * @return the list of follow
     */
    @Override
    public List<Follow> findAllFollowsBySpaceId(Long spaceId) {
        return this.followRepository.findAllByFollowedSpaceId(spaceId);
    }

    /**
     * Find all spaces followed by the specific user.
     *
     * @param user the User object
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
     * @param space the Space object
     * @return the list of user
     */
    @Override
    public List<User> findAllFollowersbySpace(Space space) {
        List<User> followers = new ArrayList<>();
        for (Follow follow : space.getFollows()) {
            // Check if the follower exists in the users table (database)
            // if yes, then add the follower to the followers list
            if (this.userService.findUserById(follow.getFollower().getId()) != null) {
                followers.add(follow.getFollower());
            } else {
                deleteFollowById(follow.getId());
            }
        }
        return followers;
    }

    /**
     * Create an invitation in the database
     *
     * @param space the Space object
     * @param email the email
     */
    @Override
    public void createInvitation(Space space, String email) {
        Invitation invitation = new Invitation(space, email);
        this.invitationRepository.save(invitation);
    }

    /**
     * Find all invitations by email.
     *
     * @param email the email
     * @return the list of invitations
     */
    @Override
    public List<Invitation> findAllInvitationsByEmail(String email) {
        return this.invitationRepository.findAllByEmail(email);
    }

    /**
     * Find all invitations by space id.
     *
     * @param spaceId the space id
     * @return the list of invitations
     */
    @Override
    public List<Invitation> findAllInvitationsBySpaceId(Long spaceId) {
        return this.invitationRepository.findAllBySpaceId(spaceId);
    }

    /**
     * Find all invited spaces by email.
     *
     * @param email the email
     * @return the list of invited spaces
     */
    @Override
    public List<Space> findAllInvitedSpacesByEmail(String email) {
        List<Invitation> invitations = this.invitationRepository.findAllByEmail(email);
        List<Space> invitedSpace = new ArrayList<>();
        if (invitations.isEmpty()) {
            return null;
        }
        for (Invitation invitation : invitations) {
            invitedSpace.add(invitation.getSpace());
        }
        return invitedSpace;
    }

    /**
     * Send the invitation email to the invitee via Spring Boot mail service.
     *
     * @param email     the email of invitee
     * @param spaceName the space name
     */
    @Override
    public void sendInvitationEmail(String email, String spaceName) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Invitation from MiniOne");
        msg.setText(String.format("Hello, you are invited to join %s. " +
                "The website url is: http://localhost:8090/", spaceName));

        // Todo: update the MiniOne Website url after deploy it to the AWS in the future

        javaMailSender.send(msg);
    }

    /**
     * Check if the "invitations" table contain a specific invitation.
     *
     * @param invitation_a the specific invitation
     * @param invitations  the list of invitations
     * @return true if invitations are equal, otherwise false
     */
    @Override
    public Boolean containInvitationInThisSpace(Invitation invitation_a, List<Invitation> invitations, Long spaceId) {
        for (Invitation invitation : invitations) {
            // invitations are equal if they have same invitee's email and same space id.
            if ((invitation.getEmail().equals(invitation_a.getEmail())) &&
                    invitation.getSpace().getId().equals(spaceId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delete all the invitations after adding user as the followers of the invited spaces.
     *
     * @param invitations the list of invitations
     */
    @Override
    public void deleteInvitations(List<Invitation> invitations) {
        for (Invitation invitation : invitations) {
            this.invitationRepository.deleteById(invitation.getId());
        }
    }

    /**
     * Merge the followed spaces and the invited space without duplicates.
     *
     * @param followedSpaces the list of the followed spaces
     * @param invitedSpaces  the list of the invited spaces
     * @return the list of the Space objects
     */
    @Override
    public List<Space> mergeFollowedSpacesAndInvitedSpaces(List<Space> followedSpaces, List<Space> invitedSpaces) {
        Set<Space> followedSpacesSet = new LinkedHashSet<>(followedSpaces);
        followedSpacesSet.addAll(invitedSpaces);

        return new ArrayList<>(followedSpacesSet);
    }

    /**
     * Set baby's age for each followed space
     *
     * @param followedSpaces the list of the followed space
     */
    @Override
    public void setEachFollowedSpaceBabyAge(List<Space> followedSpaces) {
        followedSpaces.forEach(followedSpace -> followedSpace.setBabyAge(followedSpace.calculateBabyAge(followedSpace
                .getBabyBirthday())));
    }

    /**
     * Set is admin (whether the user is admin of the space) for each followed space
     *
     * @param followedSpaces the followed space
     * @param follower       the follower
     */
    @Override
    public void setEachFollowedSpaceIsAdmin(List<Space> followedSpaces, User follower) {
        followedSpaces.forEach(followedSpace -> followedSpace.setIsAdmin(follower));
    }

    /**
     * Set space url for each followed space.
     *
     * @param followedSpaces the list of the followed spaces
     */
    @Override
    public void setEachFollowedSpaceUrl(List<Space> followedSpaces) {
        followedSpaces.forEach(followedSpace -> followedSpace.setSpaceUrl("/spaces/" + followedSpace.getId()));
    }

    /**
     * Set unfollow url for each followed space.
     *
     * @param followedSpaces the list of the followed spaces
     * @param follower       the follower
     */
    @Override
    public void setEachFollowedSpaceUnfollowUrl(List<Space> followedSpaces, User follower) {
        followedSpaces.stream().filter(followedSpace -> followedSpace.getOwner() != follower)
                .forEach(followedSpace -> followedSpace.setUnfollowUrl("/follows/" + follower.getId() + "/unfollow/" + followedSpace.getId()));
    }
}
