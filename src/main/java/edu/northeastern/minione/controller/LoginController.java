package edu.northeastern.minione.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.minione.forms.LoginForm;
import edu.northeastern.minione.entity.User;
import edu.northeastern.minione.service.AmazonClientService;
import edu.northeastern.minione.service.AmazonS3ImageService;
import edu.northeastern.minione.service.UserService;

/**
 * This is the login controller.
 * <p>
 * This controller starts by using the standard @controller annotation.
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private AmazonClientService amazonClientService;

    @Autowired
    private AmazonS3ImageService amazonS3ImageService;

    /**
     * Display the login form.
     *
     * @param loginForm the login form
     * @return the corresponding modelAndView
     */
    @GetMapping("/users/login")
    public ModelAndView login(LoginForm loginForm) {
        ModelAndView modelAndView = new ModelAndView();
        // User doesn't need to re-enter credentials
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            modelAndView.setViewName("users/login");
        } else {
            User user = this.userService.findUserByUserName(auth.getName());
            Long userId = user.getId();
            modelAndView.setViewName("users/login-success");
            modelAndView.addObject("userId", userId);
        }
        return modelAndView;
    }

    /**
     * Setup the user's registration form.
     *
     * @return the corresponding modelAndView
     */
    @GetMapping("/users/register")
    public ModelAndView showRegistrationForm() {
        return new ModelAndView("users/register", "user", new User());
    }

    /**
     * Create user in the database, then direct the user to the registration success page
     *
     * @param user          the User object
     * @param bindingResult the object that contains newly submitted data by the user
     * @param multipartFile an uploaded file received in a multipart request
     * @return the corresponding modelAndView
     * @throws IOException
     */
    @PostMapping("/users/register")
    public ModelAndView register(@Valid User user, BindingResult bindingResult,
                                 @RequestParam("image") MultipartFile multipartFile) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        User userExists = this.userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            modelAndView.setViewName("users/register");
            bindingResult.rejectValue("userName", "error.user", "User exists, please try another name.");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("users/register");
            return modelAndView;
        }
        this.userService.createUser(user);

        modelAndView.setViewName("users/register-success");

        // Upload one image as user profile image to S3, and set ImageUrl (pre-signed Url)
        if (multipartFile.getSize() > 0) {
            try {
                this.amazonS3ImageService.uploadUserProfileImageToS3(multipartFile, user);
                modelAndView.addObject("ImageUrl", amazonS3ImageService.
                        getGeneratePresignedUrl(user.getUserImageObjectKey()).toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ModelAndView("/users/register");
            }
        }
        return modelAndView;
    }

}
