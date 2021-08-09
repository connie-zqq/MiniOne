package edu.northeastern.minione.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;
import edu.northeastern.minione.service.FollowService;
import edu.northeastern.minione.service.UserService;

@Controller
public class FollowController {

    // Todo: follow list

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @GetMapping("/follows/{user-id}")
    public ModelAndView followIndex(@PathVariable("user-id") Long userId) {

        User user = this.userService.findUserById(userId);

        // Find a list of all spaces followed by the user
        List<Space> followedSpaces = this.followService.findAllSpacesFollowedByUser(user);

        ModelAndView modelAndView = new ModelAndView("follows/index");

        modelAndView.addObject("userName", user.getUserName());
        modelAndView.addObject("followedSpaces", followedSpaces);

        return modelAndView;
    }

}
