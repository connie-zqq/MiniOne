package edu.northeastern.minione.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.minione.model.Comment;
import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;
import edu.northeastern.minione.service.CommentService;
import edu.northeastern.minione.service.FollowService;
import edu.northeastern.minione.service.MomentService;
import edu.northeastern.minione.service.UserService;

@Controller
public class CommentController {

    @Autowired
    private MomentService momentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FollowService followService;

    /**
     * Setup the comment-form form to the user.
     *
     * @param spaceId  the space id of the current page
     * @param momentId the moment id of the current page
     * @return the corresponding modelAndView that contains all the info needed to display the comment-form
     */
    @GetMapping("/spaces/{space-id}/moments/{moment-id}/add-comments")
    public ModelAndView showAddComments(@PathVariable("space-id") Long spaceId, @PathVariable("moment-id") Long momentId) {

        ModelAndView modelAndView = new ModelAndView("spaces/comment-form");

        // Get the user info from the auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

        // Find the space by space id obtained by url parsing
        Space space = this.momentService.findSpaceById(spaceId);

        // ### 2 cases (errors) to redirect to the "follow" page: ###
        // - a. If the space does not exist, then redirect to the user's follow page
        if (space == null) {
            System.out.println("Sorry, we cannot find the space");
            return new ModelAndView(String.format("redirect:/follows/%d", userId));
        }
        // Find a list of followers of the space
        List<User> followers = this.followService.findAllFollowersbySpace(space);

        // - b. If the space exist, but the logged-in user does not follow the space, then redirect to the user's follow page
        if (!followers.contains(user)) {
            System.out.println("Sorry, you cannot access it since you are not the follower of this space.");
            return new ModelAndView(String.format("redirect:/follows/%d", userId));
        }

        // Find the moment by moment id which is obtained by url parsing
        Moment moment = this.momentService.findMomentById(momentId);

        // ### 2 case to redirect to the "space homepage": ###
        // - a. If the moment does not exist, then redirect the space homepage
        if (moment == null) {
            System.out.println("Sorry, we cannot find the moment");
            return new ModelAndView(String.format("redirect:/spaces/%d", spaceId));
        }
        // - b. If the moment does not belong to the space, then redirect the space homepage
        if (!moment.getSpace().getId().equals(spaceId)) {
            System.out.println("Sorry, we cannot find the moment");
            return new ModelAndView(String.format("redirect:/spaces/%d", spaceId));
        }

        modelAndView.addObject("comment", new Comment());
        modelAndView.addObject("momentId", momentId);
        modelAndView.addObject("spaceId", spaceId);
        modelAndView.addObject("userId", user.getId());

        return modelAndView;
    }

    /**
     * Create the new comment in the database, and redirect the user to the "space homepage" once the comment created
     * successfully.
     *
     * @param spaceId       the space id
     * @param momentId      the moment id
     * @param comment       the Comment object
     * @param bindingResult the object that contains newly submitted data by the user
     * @return
     */
    @PostMapping("/spaces/{space-id}/moments/{moment-id}/add-comments")
    public ModelAndView addComment(@PathVariable("space-id") Long spaceId, @PathVariable("moment-id") Long momentId,
                                   @ModelAttribute Comment comment, BindingResult bindingResult) {
        Moment moment = this.momentService.findMomentById(momentId);

        // Get the logged-in user from the authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        if (user == null) {
            bindingResult.rejectValue("author", "error.post", "Author cannot be null");
        }

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("spaces/add-moment-error");
            modelAndView.addObject("spaceId", spaceId);
            modelAndView.addObject("momentId", momentId);
            return modelAndView;
        }

        modelAndView.addObject("comment", new Comment());

        // Set the user as the author of the comment
        comment.setAuthor(user);
        // Set the comment as the moment's comment
        comment.setMoment(moment);
        // Add comment and save the comment info to the MySQL database
        this.commentService.createComment(comment);
        moment.setComments(this.commentService.findAllCommentsByMomentId(momentId));

        modelAndView.setViewName(String.format("redirect:/spaces/%d", spaceId));

        return modelAndView;
    }
}
