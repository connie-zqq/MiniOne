package edu.northeastern.minione.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This is the homepage controller.
 * <p>
 * This controller starts by using the standard @controller annotation.
 */
@Controller
public class HomeController {

    /**
     * Display the "homepage".
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/", "/home"})
    public String index(Model model) {
        return "index";
    }
}

