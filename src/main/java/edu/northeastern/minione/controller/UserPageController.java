package edu.northeastern.minione.controller;

import edu.northeastern.minione.model.User;
import edu.northeastern.minione.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserPageController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    public String index(Model model, @PageableDefault(sort = {"userName"}, value = 5) Pageable pageable){
        // Get the content of the table, TODO. find a way to paginate
        Page<User> users = this.userService.findAll(pageable);

        // Define variables to be passed to view
        model.addAttribute("users", users);
        // Return the view model itself
        return "user/userpage";
    }
}
