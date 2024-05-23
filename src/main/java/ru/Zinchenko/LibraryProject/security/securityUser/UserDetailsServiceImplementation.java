package ru.Zinchenko.LibraryProject.security.securityUser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.security.repo.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {
    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userFromDb = repo.findByUsername(username);
        if(userFromDb.isEmpty())
            throw new UsernameNotFoundException("User not found!");

        return new SecurityUser(userFromDb.get());
    }
}
