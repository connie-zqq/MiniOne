package edu.northeastern.minione.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.service.MomentService;
import edu.northeastern.minione.service.UserService;

@Controller
public class MomentController {

    @Autowired
    private MomentService momentService;

    @Autowired
    private UserService userService;

    /**
     * Render the form. The GET for displaying data in the form. Notice:
     *
     * @return
     */
    @GetMapping(value = "/space/create-space")
    public ModelAndView showCreateSpaceForm() {
        return new ModelAndView("space/create-space", "space", new Space());
    }

    /**
     * Display the submitted data.
     *
     * @param space
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/space/create-space")
    public ModelAndView submitCreateSpaceForm(@ModelAttribute("space") Space space, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "space/error";
//        }

        model.addAttribute("spaceName", space.getSpaceName());
        model.addAttribute("spaceDescription", space.getSpaceDescription());

        return new ModelAndView("space/create-space-success", "space", model);
    }

//    /**
//     * Create a space via form's submit. The Post for the creation operation.
//     * @PostMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.POST).
//     *
//     * @param space
//     * @param bindingResult
//     * @return
//     */
//    @PostMapping("/space/create-space")
//    public ModelAndView createSpace(@Valid Space space, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView("space/create-space");
//        // Perform validation
//        if (space.getSpaceName().isEmpty()) {
//            bindingResult.rejectValue("spaceName", "error.post", "Space name cannot be empty");
//            // rejectValue(String field, String errorCode, String defaultMessage)
//        }
//        if (space.getSpaceDescription().isEmpty()) {
//            bindingResult.rejectValue("spaceDescription", "error.post", "Space description cannot be empty");
//        }
//        // Get the owner of the space
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        User user = this.userService.findUserByUserName(auth.getName());
//        User user = new User("connie", "Qiangqiang", "Zhang", "zhang.qiang@northeastern.edu");
//        if (user == null) {
//            bindingResult.rejectValue("owner", "error.post", "The owner cannot be null");
//        }
//        if (!bindingResult.hasErrors()) {
//            space.setOwner(user);
//            this.momentService.createSpace(space);
//            modelAndView.addObject("successMessage", "Space has been created");
//            modelAndView.addObject("space", new Space());
//        }
//        return modelAndView;
//    }
}
