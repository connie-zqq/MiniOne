package edu.northeastern.minione.controller;

import java.io.IOException;
import java.util.List;

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
import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;
import edu.northeastern.minione.service.AmazonClientService;
import edu.northeastern.minione.service.AmazonS3ImageService;
import edu.northeastern.minione.service.FollowService;
import edu.northeastern.minione.service.MomentService;
import edu.northeastern.minione.service.UserService;

@Controller
public class MomentController {

    @Autowired
    private MomentService momentService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private AmazonS3ImageService amazonS3ImageService;

    /**
     * Display the space's form.
     *
     * @return
     */
    @GetMapping(value = "/spaces/create-space")
    public ModelAndView showCreateSpaceForm() {
        return new ModelAndView("spaces/create-space", "space", new Space());
    }

    /**
     * Save the input information of the space into database. Display the submitted data.
     *
     * @param space
     * @param bindingResult
     * @return
     */
    @PostMapping("/spaces/create-space")
    public ModelAndView creatSpace(@ModelAttribute("space") Space space, BindingResult bindingResult) {
        // Perform validation
        if (space.getSpaceName().isEmpty()) {
            bindingResult.rejectValue("spaceName", "error.post", "Space name cannot be empty");
        }

        // Get the owner of the space from the auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        if (user == null) {
            bindingResult.rejectValue("owner", "error.post", "Owner cannot be null");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("spaces/create-space-error");
        }
        space.setOwner(user);

        // Create space and save the space info in the MySQL server (generate the space id)
        this.momentService.createSpace(space);

        // Add follow (the owner is also the follower of the space)
        this.followService.addFollow(user, space);

        ModelAndView modelAndView = new ModelAndView("spaces/create-space-success");
        // user info
        assert user != null;
        // space info
        modelAndView.addObject("spaceId", space.getId());
        modelAndView.addObject("spaceOwnerUserName", space.getOwner().getUserName());
        modelAndView.addObject("spaceName", space.getSpaceName());
        modelAndView.addObject("spaceDescription", space.getSpaceDescription());
        modelAndView.addObject("spaceCreatedDateTime", space.getCreatedDateTime());
        // baby info
        modelAndView.addObject("babyName", space.getBabyName());
        modelAndView.addObject("babyBirthday", space.getBabyBirthday());
        modelAndView.addObject("babyGender", space.getBabyGender());
        modelAndView.addObject("babyResidence", space.getBabyResidence());

        return modelAndView;
    }

    /**
     * Show the space Homepage.
     *
     * @param spaceId the ID of the space
     * @return
     */
    @GetMapping("/spaces/{space-id}")
    public ModelAndView spaceIndex(@PathVariable("space-id") Long spaceId, @PageableDefault(sort = {"id"}, value = 2,
            direction = Sort.Direction.DESC) Pageable pageable) {

        // Find the space by space id which is obtained by url parsing
        Space space = this.momentService.findSpaceById(spaceId);

        // Get the viewer of the space from the auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User viewer = this.userService.findUserByUserName(auth.getName());
        Long userId = viewer.getId();

        // If the space is null, then redirect to the follow page
        if (space == null) {
            // Todo: notification
            System.out.println("Sorry, we cannot find the space");
            String viewName = String.format("redirect:/follows/%d", userId);
            return new ModelAndView(viewName);
        }

        // Find a list of followers of the space
        List<User> followers = this.followService.findAllFollowersbySpace(space);

        // If the viewer is not one of the followers of the space, then redirect to the follow page
        if (!followers.contains(viewer)) {
            System.out.println("Sorry, you cannot access it since you are not the follower of this space.");
            String viewName = String.format("redirect:/follows/%d", userId);
            return new ModelAndView(viewName);
        }

        // Find a list of moments with pagination
        Page<Moment> moments = this.momentService.findAllMomentsBySpaceId(spaceId, pageable);

        ModelAndView modelAndView = new ModelAndView("spaces/index");

        // Space info
        modelAndView.addObject("spaceId", space.getId());
        modelAndView.addObject("spaceName", space.getSpaceName());
        modelAndView.addObject("spaceDescription", space.getSpaceDescription());
        modelAndView.addObject("spaceCreatedDateTime", space.getCreatedDateTime());
        modelAndView.addObject("numOfFollowers", followers.size());   // number of followers - families

        // Baby info
        modelAndView.addObject("babyName", space.getBabyName());

        // Baby Age
        String babyAge = space.claculateBabyAge(space.getBabyBirthday());
        modelAndView.addObject("babyAge", babyAge);
        modelAndView.addObject("babyResidence", space.getBabyResidence());

        // Moment info
        modelAndView.addObject("moments", moments);
        moments.forEach(moment -> {
            moment.getPhotos().forEach(photo -> photo.setSignedUrl(amazonS3ImageService.getGeneratePresignedUrl(photo.getObjectKey()).toString()));
        });
        System.out.println("#####" + modelAndView.getModel());

        return modelAndView;
    }


//    /**
//     * Delete the space. Only the owner of space can delete the space.
//     *
//     * @param id space id
//     * @return
//     */
//    @RequestMapping("/spaces/delete-space/{id}")
//    public String deleteSpace(@PathVariable("id") Long id) {
//        Optional<Space> space = this.momentService.findSpaceById(id);
//        if (!space.isPresent()) {
//            // Todo: notification - double check delete or not
//            System.out.println("Sorry, we cannot find the space");
//            // Todo: change the url - follow page
//            return "redirect:/";
//        }
//        // Todo: waiting for the authentication
////        Get the owner of the space
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = this.userService.findUserByUserName(auth.getName());
//
//        // Only owner can delete the space
//        if (user.getId().equals(space.get().getOwner().getId())) {
//            this.momentService.deleteSpaceById(id);
//            // 所有与之相关的followers 都会自动取关么 follow表会自动更新么
//            // how to double check if it need to be deleted - confirm?
//        }
//        // Todo: change the url of redirect
//        return "redirect:/user/login/";
//    }

    /**
     * Display the "create moment" form.
     *
     * @return a new ModelAndView
     */
    @GetMapping(value = "/spaces/{space-id}/moments/add-moment")
    public ModelAndView showAddMomentForm(@PathVariable("space-id") Long spaceId) {
        ModelAndView modelAndView = new ModelAndView("spaces/add-moment");
        modelAndView.addObject("moment", new Moment());
        modelAndView.addObject("spaceId", spaceId);
        return modelAndView;
    }

    @PostMapping("/spaces/{space-id}/moments/add-moment")
    public ModelAndView addMoment(@PathVariable("space-id") Long spaceId, @ModelAttribute("moment") Moment moment, BindingResult bindingResult,
                                  @RequestParam("image") MultipartFile multipartFile) throws IOException {
        Space space = this.momentService.findSpaceById(spaceId);

        // Perform validation
        if (moment.getMomentContent().isEmpty()) {
            bindingResult.rejectValue("momentContent", "error.post", "Moment content cannot be empty");
        }

        // Get the author of the moment from the auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        if (user == null) {
            bindingResult.rejectValue("author", "error.post", "Author cannot be null");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("spaces/add-moment-error");
        }

        // Set the user as the author of the moment
        moment.setAuthor(user);

        // Set the moment is the space's moment
        moment.setSpace(space);

        // Add moment and save the moment info in the MySQL server
        this.momentService.createMoment(moment);

        // Upload one image to S3, and set ImageUrl
        AmazonImage amazonImage = null;
        if (!multipartFile.isEmpty()) {
            amazonImage = this.amazonS3ImageService.uploadImageToS3(multipartFile, moment);
        }
//        List<MultipartFile> images = Arrays.asList(multipartFiles);
//        if (multipartFiles.length != 0) {
//            this.amazonS3ImageService.uploadImages(images, moment);
//        }

        ModelAndView modelAndView = new ModelAndView("spaces/add-moment-success");

        modelAndView.addObject("spaceId", space.getId());
        modelAndView.addObject("momentAuthor", moment.getAuthor().getUserName());
        modelAndView.addObject("momentTitle", moment.getMomentTitle());
        modelAndView.addObject("momentContent", moment.getMomentContent());
        modelAndView.addObject("ImageUrl", amazonS3ImageService
                .getGeneratePresignedUrl(amazonImage.getObjectKey()).toString());
//        modelAndView.addObject("images", images);

        modelAndView.addObject("momentCreatedDateTime", moment.getCreatedDateTime());

        return modelAndView;
    }


    // Todo: invite user via Email (follow)

    // Todo: unfollow space ++++

    // Todo: edit space (if you are the owner of the page)

    // Todo: edit moment  (if you are the author or owner)

    // Todo: delete moment (if you are the author or owner)

    // Todo: likes

    // Todo: comments

    // Todo: personalize your home page

}
