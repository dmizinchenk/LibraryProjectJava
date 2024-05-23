package ru.Zinchenko.LibraryProject.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.security.repo.UserRepository;
import ru.Zinchenko.LibraryProject.security.securityUser.UserDetailsServiceImplementation;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserRepository repo;
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> maybe =  repo.findByUsername(user.getUsername());
        if(maybe.isPresent())
        {
            errors.rejectValue("username", "Такой пользователь уже существует");
        }
    }
}
