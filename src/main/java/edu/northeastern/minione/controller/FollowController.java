package edu.northeastern.minione.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.minione.entity.Invitation;
import edu.northeastern.minione.entity.Space;
import edu.northeastern.minione.entity.User;
import edu.northeastern.minione.service.FollowService;
import edu.northeastern.minione.service.MomentService;
import edu.northeastern.minione.service.UserService;

/**
 * This is the follow controller.
 * This controller starts by using the standard @controller annotation.
 */
@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private MomentService momentService;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * The "follow" page (babies) lists all the spaces followed by user.
     *
     * @param userId the user id
     * @return the corresponding modelAndView
     */
    @GetMapping("/follows/{user-id}")
    public ModelAndView followIndex(@PathVariable("user-id") Long userId) {

        // Get current logged-in user from authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        String userEmail = user.getEmail();

        // Get the follower by parsing the URL
        User follower = this.userService.findUserById(userId);

        // ## Check if the follower obtained from the URL exist or not:
        // - If the follower does not exist in the "users" table, then redirect to the follow page error page
        if (follower == null) {
            return new ModelAndView("follows/follow-page-error");
        }

        // ## Check if the logged-in user is the same person who visit the url：##
        // - If not, then redirect to the logged-in user's follow page
        if (!user.equals(follower)) {
            System.out.println("The logged-in user is not the follower of these spaces");
            return new ModelAndView(String.format("redirect:/follows/%d", user.getId()));
        }

        List<Invitation> invitations = this.followService.findAllInvitationsByEmail(userEmail);

        List<Space> invitedSpaces = this.followService.findAllInvitedSpacesByEmail(userEmail);

        List<Space> followedSpaces = this.followService.findAllSpacesFollowedByUser(follower);

        // ## Check if the user has invitation or not: ##
        // - a. If the user has invitation
        if (invitedSpaces != null) {
            // Merge the invitedSpaces list and the followedSpaces list without duplicates
            followedSpaces = this.followService.mergeFollowedSpacesAndInvitedSpaces(followedSpaces, invitedSpaces);

            // Add the user to become the follower of multiple invited spaces;
            this.followService.addFollows(user, invitedSpaces);
            // Delete invitation from database after adding the user as the followers.
            this.followService.deleteInvitations(invitations);
        }
        // - b. If the login user does not have any extra invitation, then display the space list
        // Notice: the new user who is not following any space will show a empty list

        ModelAndView modelAndView = new ModelAndView("follows/index");

        modelAndView.addObject("userId", user.getId());
        modelAndView.addObject("followedSpaces", followedSpaces);

        this.followService.setEachFollowedSpaceBabyAge(followedSpaces);
        this.followService.setEachFollowedSpaceUrl(followedSpaces);
        this.followService.setEachFollowedSpaceIsAdmin(followedSpaces, follower);
        // Only the follower who is not the admin of the space can unfollow the space
        this.followService.setEachFollowedSpaceUnfollowUrl(followedSpaces, follower);

        return modelAndView;
    }

    /**
     * The "followers" page lists all the followers who are following this space.
     *
     * @return the corresponding modelAndView
     */
    @GetMapping("/spaces/{space-id}/followers")
    public ModelAndView getFollowers(@PathVariable("space-id") Long spaceId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

        Space space = this.momentService.findSpaceById(spaceId);

        // ### 2 cases (errors) to redirect to the "follow" page: ###
        // - a. If the space does not exist, then redirect to the user's follow page
        if (space == null) {
            System.out.println("Sorry, we cannot find the space");
            return new ModelAndView(String.format("redirect:/follows/%d", userId));
        }
        // Find a list of all follower who are following this page
        // in this method, we also check if the follower exists in the user table (database)
        List<User> followers = this.followService.findAllFollowersbySpace(space);
        // - b. If the space exist, but the logged-in user does not follow the space, then redirect to the user's follow page
        if (!followers.contains(user)) {
            System.out.println("Sorry, you cannot access it since you are not the follower of this space.");
            return new ModelAndView(String.format("redirect:/follows/%d", userId));
        }

        ModelAndView modelAndView = new ModelAndView("follows/followers");

        modelAndView.addObject("followers", followers);
        modelAndView.addObject("spaceId", spaceId);
        modelAndView.addObject("userId", userId);

        return modelAndView;
    }

    /**
     * In the "follow" page, only the followers who are not the admin of the spaces can unfollow spaces. There will
     * be an unfollow link for each followedSpace.
     *
     * @param userId  the user id
     * @param spaceId the space id
     * @return the corresponding modelAndView
     */
    @RequestMapping("/follows/{user-id}/unfollow/{space-id}")
    public ModelAndView unfollow(@PathVariable("user-id") Long userId, @PathVariable("space-id") Long spaceId) {

        // Check if the logged in user obtained from authentication is the same person who visit the url:
        // Get current logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        User follower = this.userService.findUserById(userId);
        Space followedSpace = this.momentService.findSpaceById(spaceId);

        // ## Check if the follower obtained from the URL exist or not:
        // - If the follower does not exist in the "users" table, then redirect to the follow page error page
        if (follower == null) {
            return new ModelAndView("follows/follow-page-error");
        }

        // ## Check if the logged-in user is the same person who visit the url：##
        // - If not, then redirect to the logged-in user's follow page
        if (!user.equals(follower)) {
            System.out.println("The logged-in user is not the follower of these spaces");
            return new ModelAndView(String.format("redirect:/follows/%d", user.getId()));
        }

        // ### 2 cases (errors) to redirect to the "follow" page: ###
        // - a. If the space does not exist, then redirect to the user's follow page
        if (followedSpace == null) {
            System.out.println("Sorry, we cannot find the space");
            return new ModelAndView(String.format("redirect:/follows/%d", userId));
        }
        // Find a list of all follower who are following this page
        // in this method, we also check if the follower exists in the user table (database)
        List<User> followers = this.followService.findAllFollowersbySpace(followedSpace);
        // - b. If the space exist, but the logged-in user does not follow the space, then redirect to the user's follow page
        if (!followers.contains(user)) {
            System.out.println("Sorry, you cannot access it since you are not the follower of this space.");
            return new ModelAndView(String.format("redirect:/follows/%d", userId));
        }

        // Only the follower who are not the owner of the space can unfollow the space (checked this situation in the
        // unfollowSpace method)
        this.followService.unfollowSpace(follower, followedSpace);

        return new ModelAndView(String.format("redirect:/follows/%d", userId));
    }

    /**
     * Setup the "invitation" form.
     *
     * @param spaceId the space id
     * @return the corresponding modelAndView
     */
    @GetMapping(value = "/spaces/{space-id}/followers/invite")
    public ModelAndView showInvitationForm(@PathVariable("space-id") Long spaceId) {
        ModelAndView modelAndView = new ModelAndView("follows/invite");
        modelAndView.addObject("spaceId", spaceId);
        modelAndView.addObject("invitation", new Invitation());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

        Space space = this.momentService.findSpaceById(spaceId);

        // ### 2 cases (errors) to redirect to the "follow" page: ###
        // - a. If the space does not exist, then redirect to the user's follow page
        if (space == null) {
            System.out.println("Sorry, we cannot find the space");
            return new ModelAndView(String.format("redirect:/follows/%d", userId));
        }
        // Find a list of all follower who are following this page
        // in this method, we also check if the follower exists in the user table (database)
        List<User> followers = this.followService.findAllFollowersbySpace(space);
        // - b. If the space exist, but the logged-in user does not follow the space, then redirect to the user's follow page
        if (!followers.contains(user)) {
            System.out.println("Sorry, you cannot access it since you are not the follower of this space.");
            return new ModelAndView(String.format("redirect:/follows/%d", userId));
        }

        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    /**
     * Save the invitation in the database. Display the submitted data.
     * The users who are the followers of the space can invite more family members to visit this space.
     *
     * @param invitation    the invitation object
     * @param bindingResult the object that contains newly submitted data by the user
     * @return the corresponding modelAndView
     */
    @PostMapping(value = "/spaces/{space-id}/followers/invite")
    public ModelAndView sendInvitation(@PathVariable("space-id") Long spaceId, @ModelAttribute("invitation") Invitation invitation,
                                       BindingResult bindingResult) {

        // Perform validation
        if (invitation.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", "error.post", "Invitee's email cannot be empty");
        }

        // Get the logged in user from authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

        Space space = this.momentService.findSpaceById(spaceId);

        ModelAndView modelAndView = new ModelAndView("follows/send-invitation-success");

        // Get the invitee's email
        String inviteeEmail = invitation.getEmail();

        List<Invitation> invitations = this.followService.findAllInvitationsBySpaceId(spaceId);

        // Check if the invitee is invited first time or not:
        // - If you first invite the family member, then create invitation and send an invitation. Otherwise, just resend
        // invitation email
        if (!followService.containInvitationInThisSpace(invitation, invitations, spaceId)) {
            // Create and save the invitation in MySQL server
            this.followService.createInvitation(space, inviteeEmail);
        }
        // - Send invitation email via Spring Boot mail service (Gmail)
        this.followService.sendInvitationEmail(inviteeEmail, space.getSpaceName());

        modelAndView.addObject("spaceId", space.getId());
        modelAndView.addObject("inviteeEmail", inviteeEmail);
        modelAndView.addObject("userId", user.getId());

        return modelAndView;
    }
}
