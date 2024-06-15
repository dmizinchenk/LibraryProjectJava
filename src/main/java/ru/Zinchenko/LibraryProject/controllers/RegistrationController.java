package ru.Zinchenko.LibraryProject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.Zinchenko.LibraryProject.security.entity.Role;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.security.util.UserValidator;
import ru.Zinchenko.LibraryProject.services.implementations.RegistrationService;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService service;
    private final UserValidator validator;

    @PostMapping()
    public String register(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes ra) {
        validator.validate(user, bindingResult);
        System.out.println(user);

        boolean hasErrors = bindingResult.hasErrors();
        if(hasErrors) {
            ra.addFlashAttribute("regError", "Такой пользователь уже существует");
            return "redirect:/login";
        }

        user.setRole(Role.ROLE_CUSTOMER);
        user.setOrders(new ArrayList<>());

        service.register(user);

        return "redirect:/";
    }
}