package ru.Zinchenko.LibraryProject.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.security.repo.UserRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Transactional
    public void register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }
}
