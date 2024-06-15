package ru.Zinchenko.LibraryProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Zinchenko.LibraryProject.security.entity.User;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping()
    public String login(@ModelAttribute("user") User userDTO){
        return "ui/pages/login";
    }

//    @PostMapping("/auth")
//    public String authorize (@RequestParam String email, @RequestParam String password, Model model){
//
//        return "redirect:/";
//    }
}
