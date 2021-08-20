package edu.northeastern.minione.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.minione.model.AmazonImage;
import edu.northeastern.minione.model.Like;
import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;
import edu.northeastern.minione.service.AmazonClientService;
import edu.northeastern.minione.service.AmazonS3ImageService;
import edu.northeastern.minione.service.CommentService;
import edu.northeastern.minione.service.FollowService;
import edu.northeastern.minione.service.MomentService;
import edu.northeastern.minione.service.UserService;

/**
 * This is the moment controller.
 * <p>
 * This controller starts by using the standard @controller annotation.
 */
@Controller
public class MomentController {

    @Autowired
    private MomentService momentService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private AmazonS3ImageService amazonS3ImageService;

    /**
     * Setup the "Create Space" form.
     * <p>
     * When you enter this URL ("/spaces/create-space"), it will send an HTTP GET request to the web app.
     * This in return triggers the execution of the showCreateSpaceForm method.
     *
     * @return both model and view in a single return value
     */
    @GetMapping("/spaces/create-space")
    public ModelAndView showCreateSpaceForm() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("spaces/create-space");

        modelAndView.addObject("space", new Space());

        // Get the logged-in user from authentication (for "babies" nav link - follow page)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userId", user.getId());

        return modelAndView;
    }

    /**
     * Creating a new space in the database ("spaces"), then display the submitted data.
     * <p>
     * After you have filled in the form fields, submitting the form triggers a HTTP POST request, that in
     * return invokes the createSpace method. If there is no mistake in submitted data, a new space will be
     * created and the user will go to a success page.
     *
     * @param space         the space object
     * @param bindingResult the object that contains newly submitted data by the user.
     * @return the corresponding modelAndView
     */
    @PostMapping("/spaces/create-space")
    public ModelAndView creatSpace(@ModelAttribute("space") Space space, BindingResult bindingResult,
                                   @RequestParam("image") MultipartFile multipartFile) throws IOException {

        // Perform validation
        if (space.getSpaceName().isEmpty()) {
            bindingResult.rejectValue("spaceName", "error.post", "Space name cannot be empty");
        }

        if (space.getBabyName().isEmpty()) {
            bindingResult.rejectValue("babyName", "error.post", "Baby name cannot be empty");
        }

        if (space.getBabyGender().isEmpty()) {
            bindingResult.rejectValue("babyGender", "error.post", "Baby Gender cannot be empty");
        }

        if (space.getBabyBirthday() == null) {
            bindingResult.rejectValue("babyBirthday", "error.post", "Baby birthday cannot be empty");
        }

        if (space.getBabyResidence().isEmpty()) {
            bindingResult.rejectValue("babyResidence", "error.post", "Baby residence cannot be empty");
        }

        if (space.getSpaceDescription().isEmpty()) {
            bindingResult.rejectValue("spaceDescription", "error.post", "Space description cannot be empty");
        }

        // Get the logged-in user from authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        if (user == null) {
            bindingResult.rejectValue("owner", "error.post", "Owner cannot be null");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("spaces/create-space-error");
        }

        // Set the user as the owner of the space
        space.setOwner(user);

        // Create space and save the space info in the MySQL server (generate the space id)
        this.momentService.createSpace(space);

        // Add follow (the owner is also the follower of the space)
        this.followService.addFollow(user, space);

        ModelAndView modelAndView = new ModelAndView("spaces/create-space-success");

        // Upload the baby profile image to S3 when the owner uploads the image
        if (multipartFile.getSize() > 0) {
            try {
                this.amazonS3ImageService.uploadBabyProfileImageToS3(multipartFile, space);

                String babyProfileImageUrl = this.momentService.getBabyProfileImageSignedUrl(space);
                modelAndView.addObject("babyProfileImageUrl", babyProfileImageUrl);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ModelAndView("redirect:/spaces/create-space");
            }
        }

        // User info
        assert user != null;
        modelAndView.addObject("userId", user.getId());
        // Space info
        modelAndView.addObject("spaceId", space.getId());
        modelAndView.addObject("spaceOwnerUserName", space.getOwner().getUserName());
        modelAndView.addObject("spaceName", space.getSpaceName());
        modelAndView.addObject("spaceDescription", space.getSpaceDescription());
        modelAndView.addObject("spaceCreatedDateTime", space.getCreatedDateTime());
        // Baby info
        modelAndView.addObject("babyName", space.getBabyName());
        modelAndView.addObject("babyBirthday", space.getBabyBirthday());
        modelAndView.addObject("babyGender", space.getBabyGender());
        modelAndView.addObject("babyResidence", space.getBabyResidence());

        return modelAndView;
    }

    /**
     * Display the space homepage.
     *
     * @param spaceId the space Id
     * @return the corresponding modelAndView
     */
    @GetMapping("/spaces/{space-id}")
    public ModelAndView spaceIndex(@PathVariable("space-id") Long spaceId, @PageableDefault(sort = {"id"}, value = 2,
            direction = Sort.Direction.DESC) Pageable pageable) {

        // Get logged-in user from authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

        // Find the space by space id which is obtained by url parsing
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

        // Find a list of moments with pagination
        Page<Moment> moments = this.momentService.findAllMomentsBySpaceId(spaceId, pageable);

        ModelAndView modelAndView = new ModelAndView("spaces/index");

        // User info - used for babies link
        modelAndView.addObject("userId", userId);

        // Space info
        modelAndView.addObject("spaceId", space.getId());
        modelAndView.addObject("spaceName", space.getSpaceName());
        modelAndView.addObject("spaceDescription", space.getSpaceDescription());
        modelAndView.addObject("spaceCreatedDateTime", space.getCreatedDateTime());
        modelAndView.addObject("numOfFollowers", followers.size());   // "families" link (space header)

        // Baby info
        modelAndView.addObject("babyName", space.getBabyName());
        // - Baby age
        String babyAge = space.calculateBabyAge(space.getBabyBirthday());
        modelAndView.addObject("babyAge", babyAge);
        // - Baby development stage
        String babyDevStage = space.getBabyDevelopmentStage(babyAge);
        modelAndView.addObject("babyDevStage", babyDevStage);
        // - Baby gender
        modelAndView.addObject("babyGender", space.getBabyGender());
        // - Baby residence
        modelAndView.addObject("babyResidence", space.getBabyResidence());

        // Baby profile photo signed Url
        if (space.getBabyImageObjectKey() != null) {
            String babyProfileImageUrl = this.momentService.getBabyProfileImageSignedUrl(space);
            modelAndView.addObject("babyProfileImageUrl", babyProfileImageUrl);
        }

        // Moment info
        modelAndView.addObject("moments", moments);

        this.momentService.setEachMomentPhotoSignedURL(moments);
        this.momentService.setEachMomentAuthorPhotoSignedURL(moments);
        this.commentService.setEachMomentFansString(moments);

        return modelAndView;
    }

    /**
     * Setup the "Add Moment" form.
     *
     * @return the corresponding modelAndView
     */
    @GetMapping(value = "/spaces/{space-id}/moments/add-moment")
    public ModelAndView showAddMomentForm(@PathVariable("space-id") Long spaceId) {
        ModelAndView modelAndView = new ModelAndView("spaces/add-moment");
        modelAndView.addObject("moment", new Moment());
        modelAndView.addObject("spaceId", spaceId);

        // Get the logged-in user from authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();
        modelAndView.addObject("userId", userId);

        // Find the space by space id which is obtained by url parsing
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

        return modelAndView;
    }

    /**
     * Create a new moment in the database ("moments"), then display the submitted data.
     *
     * @param spaceId        the space id
     * @param moment         the Moment object
     * @param bindingResult  the object that contains newly submitted data by the user
     * @param multipartFiles a list of the multipartFiles
     * @return the corresponding modelAndView
     */
    @PostMapping("/spaces/{space-id}/moments/add-moment")
    public ModelAndView addMoment(@PathVariable("space-id") Long spaceId, @ModelAttribute("moment") Moment moment,
                                  BindingResult bindingResult,
                                  @RequestParam("images") MultipartFile[] multipartFiles) {

        Space space = this.momentService.findSpaceById(spaceId);

        // Perform validation
        if (moment.getMomentContent().isEmpty()) {
            bindingResult.rejectValue("momentContent", "error.post", "Moment content cannot be empty");
        }

        // Get the logged-in user from authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        if (user == null) {
            bindingResult.rejectValue("author", "error.post", "Author cannot be null");
        }

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("spaceId", spaceId);
            return new ModelAndView("spaces/add-moment-error");
        }
        // Set the user as the author of the moment
        moment.setAuthor(user);

        // Set the moment is the space's moment
        moment.setSpace(space);

        // Add moment and save the moment info in the MySQL server
        this.momentService.createMoment(moment);

        // After adding moment, redirect the space homepage
        modelAndView.setViewName(String.format("redirect:/spaces/%d", spaceId));

        // Upload several moment images to S3
        // To make sure that each input field has the same name so that it can be accessed as an
        // array of MultipartFile
        List<MultipartFile> files = Arrays.asList(multipartFiles);
        List<AmazonImage> momentImages = new ArrayList<>();
        // if the added moment has photos, then upload images
        if (files.get(0).getSize() > 0) {
            try {
                momentImages = this.amazonS3ImageService.uploadImages(files, moment);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ModelAndView(String.format("redirect:/spaces/%d/moments/add-moment", spaceId));
            }
        }

        return modelAndView;
    }

    /**
     * Display the "edit moment" form.
     *
     * @return the corresponding modelAndView
     */
    @GetMapping("/spaces/{space-id}/edit-moment/{moment-id}")
    public ModelAndView showEditMomentForm(@PathVariable("space-id") Long spaceId, @PathVariable("moment-id") Long momentId) {

        Moment moment = this.momentService.findMomentById(momentId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

        // Find the space by space id which is obtained by url parsing
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

        // ### 2 case to redirect to the space homepage: ###
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

        User spaceOwner = space.getOwner();

        User momentAuthor = moment.getAuthor();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("momentId", moment.getId());
        modelAndView.addObject("spaceId", spaceId);
        modelAndView.addObject("userId", userId);

        if (user.equals(spaceOwner) || user.equals(momentAuthor)) {
            modelAndView.addObject("moment", moment);
            modelAndView.setViewName("spaces/edit-moment");
        } else {
            modelAndView.setViewName("spaces/edit-moment-fail");
        }
        return modelAndView;
    }

    /**
     * Edit the moment and update the info in the database ("moments").
     *
     * @param spaceId       the space id
     * @param momentId      the moment id
     * @param moment        the Moment object
     * @param bindingResult the object that contains newly submitted data by the user
     * @return the corresponding modelAndView
     */
    @PostMapping("/spaces/{space-id}/edit-moment/{moment-id}")
    public ModelAndView editMoment(@PathVariable("space-id") Long spaceId, @PathVariable("moment-id") Long momentId,
                                   @Valid Moment moment, BindingResult bindingResult) {

        Moment moment1 = this.momentService.findMomentById(momentId);
        Space space = this.momentService.findSpaceById(spaceId);


        if (moment.getMomentTitle().isEmpty()) {
            bindingResult.rejectValue("momentTitle", "error.post", "Title cannot be empty");
        }
        if (moment.getMomentContent().isEmpty()) {
            bindingResult.rejectValue("momentContent", "error.post", "Content cannot be empty");
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView("spaces/add-moment-error");
        }

        moment1.setMomentTitle(moment.getMomentTitle());
        moment1.setMomentContent(moment.getMomentContent());
        this.momentService.editMoment(moment1);

        ModelAndView modelAndView = new ModelAndView("spaces/edit-moment");

        modelAndView.addObject("spaceId", this.momentService.findSpaceById(spaceId).getId());
        modelAndView.addObject("successMessage", "Moment has been updated");
        modelAndView.addObject("moment", moment1);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userId", user.getId());

        return modelAndView;
    }

    /**
     * Delete the moment. Don't need to check the user's authority again, as it's checked when entered the edit page.
     * After deleting the moment, the like will also be deleted from the database ("likes").
     *
     * @param spaceId  the space id
     * @param momentId the moment id
     * @return the corresponding modelAndView
     */
    @GetMapping("/spaces/{space-id}/delete-moment/{moment-id}")
    public ModelAndView deleteMoment(@PathVariable("space-id") Long spaceId, @PathVariable("moment-id") Long momentId) {

        Moment moment = this.momentService.findMomentById(momentId);
        Space space = this.momentService.findSpaceById(spaceId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

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

        // ### 2 case to redirect to the space homepage: ###
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

        // delete the moment， and its likes, comments and photos will also be deleted automatically from the database
        this.momentService.deleteMomentById(momentId);

        ModelAndView modelAndView = new ModelAndView("spaces/delete-moment-success");

        modelAndView.addObject("spaceId", spaceId);
        modelAndView.addObject("userId", user.getId());

        return modelAndView;
    }

    /**
     * Give like to the specific moment or cancel like if the user has liked the moment.
     *
     * @param spaceId  the space id
     * @param momentId the moment id
     * @return the corresponding modelAndView
     */
    @GetMapping("/spaces/{space-id}/moments/{moment-id}/like")
    public ModelAndView giveLike(@PathVariable("space-id") Long spaceId, @PathVariable("moment-id") Long momentId) {

        Moment moment = this.momentService.findMomentById(momentId);
        Space space = this.momentService.findSpaceById(spaceId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

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

        // ### 2 case to redirect to the space homepage: ###
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

        Like existedLike = this.commentService.findLikeByUserAndMoment(user, moment);

        if (existedLike == null) {
            Like like = new Like(user, moment);
            this.commentService.Like(like);
        } else {
            this.commentService.cancelLike(existedLike.getId());
        }

        return new ModelAndView(String.format("redirect:/spaces/%d", spaceId));
    }

    /**
     * In the "photo wall" page, display the photo wall of the space.
     * The photos are collected from the moments in the space.
     *
     * @param spaceId the space Id
     * @return the corresponding modelAndView
     */
    @GetMapping("/spaces/{space-id}/photo-wall")
    public ModelAndView getPhotoWall(@PathVariable("space-id") Long spaceId) {

        Space space = this.momentService.findSpaceById(spaceId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        Long userId = user.getId();

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

        // Find all photo by Space Id
        List<AmazonImage> photos = this.momentService.findAllPhotosBySpaceId(spaceId);

        ModelAndView modelAndView = new ModelAndView("spaces/photo-wall");

        modelAndView.addObject("userId", user.getId());
        modelAndView.addObject("spaceId", spaceId);
        modelAndView.addObject("photos", photos);
        this.momentService.setEachPhotoSignedUrl(photos);

        return modelAndView;
    }
}
