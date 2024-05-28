package ru.Zinchenko.LibraryProject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.Zinchenko.LibraryProject.models.DTO.UserDTO;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.services.implementations.UserServiceImplementation;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserServiceImplementation userService;

    @GetMapping()
    public String listUsers(Model model){
        model.addAttribute("allUsers", userService.findAll());
        return "ui/pages/admin/list";
    }

    @GetMapping("/changeLock/{id}")
    public String changeLockUser(@PathVariable("id") int id, Model model){
        User user =  userService.findOne(id);
        user.setBloked(!user.isBloked());
        userService.save(user);
        return listUsers(model);
    }

    @GetMapping("/add")
    public String regPage(@ModelAttribute("user") UserDTO user, Model model){
        model.addAttribute("isRegister", true);
        return "/ui/pages/admin/newUser";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") UserDTO dto){
        userService.save(User.getInstanceFromDTO(dto));
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id, Model model){
        model.addAttribute("isRegister", false);
        model.addAttribute("user", userService.findOne(id));
        return "/ui/pages/admin/newUser";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@ModelAttribute("user") User user){
        userService.updateWithPassword(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String delete (@PathVariable("id") int id){
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
