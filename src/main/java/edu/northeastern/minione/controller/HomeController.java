package edu.northeastern.minione.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.service.MomentService;

@Controller
public class HomeController {

    @Autowired
    private MomentService spaceService;

    @RequestMapping("/home")
    public String index(Model model) {   // model can supply attributes used for rendering views
        List<Space> spaces = this.spaceService.findAll();

        model.addAttribute("spaces", spaces);

        return "home/index";
    }
}
