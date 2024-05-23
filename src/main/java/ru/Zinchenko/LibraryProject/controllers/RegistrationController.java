package ru.Zinchenko.LibraryProject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Zinchenko.LibraryProject.security.entity.Role;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.security.util.UserValidator;
import ru.Zinchenko.LibraryProject.services.implementations.RegistrationService;
import ru.Zinchenko.LibraryProject.services.interfaces.UserService;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService service;
    private final UserValidator validator;

    @PostMapping
    public String register(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);

        if(bindingResult.hasErrors())
            return "ui/pages/login";

        user.setRole(Role.ROLE_CUSTOMER);
        user.setBooks(new ArrayList<>());

        service.register(user);

        return "redirect:/";
    }
}