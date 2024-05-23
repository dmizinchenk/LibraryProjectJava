package ru.Zinchenko.LibraryProject.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Zinchenko.LibraryProject.security.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String userName);
}
