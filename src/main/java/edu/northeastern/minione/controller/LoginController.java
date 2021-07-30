package edu.northeastern.minione.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.minione.forms.LoginForm;
import edu.northeastern.minione.model.User;
import edu.northeastern.minione.service.UserService;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * Shows login form
     * @param loginForm
     * @return
     */
    @RequestMapping("/user/login")
    public String login(LoginForm loginForm){
        // User doesn't need to re-enter credentials
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( (auth instanceof AnonymousAuthenticationToken) ) {
            return "user/login";
        } else {
            return "redirect:/";
        }
    }
    /**
     * Display user's registration form
     * @return
     */
    @RequestMapping("/user/register")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject(user);
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @RequestMapping(value = "user/register", method = RequestMethod.POST)
    public ModelAndView registration(@Valid User user, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        User userExists = this.userService.findUserByUserName(user.getUserName());
        if( userExists != null ){
            modelAndView.setViewName("user/register");
            bindingResult.rejectValue("userName", "error.user", "User exists, please try another name.");
        }
        if( !bindingResult.hasErrors() ){
            this.userService.createUser(user);
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("user/register_success");
        } else {
            modelAndView.setViewName("user/register");
        }
        return modelAndView;
    }

}
