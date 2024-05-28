package ru.Zinchenko.LibraryProject.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Zinchenko.LibraryProject.security.entity.Role;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.security.repo.UserRepository;
import ru.Zinchenko.LibraryProject.services.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
//    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserRepository repository;

    @Autowired
    public UserServiceImplementation(UserRepository repository) {
        this.repository = repository;
    }

    public User findByUsername(String username){
        return repository.findByUsername(username).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findOne(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        if (repository.findByUsername(user.getUsername()) == null) {
            return null;
        }
//        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public User update(User user) {
        if (!repository.existsById(user.getId())) {
            return null;
        }
        User toUpdate = findOne(user.getId());

        toUpdate.setName(user.getName());
        toUpdate.setUsername(user.getUsername());
        toUpdate.setBooks(user.getBooks());
        toUpdate.setBloked(user.isBloked());
        toUpdate.setRole(user.getRole());
        toUpdate.setOrders(user.getOrders());

        repository.save(toUpdate);
        return toUpdate;
    }

    public User updateWithPassword(User user){
        User toUpdate = update(user);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        toUpdate.setPassword(encoder.encode(user.getPassword()));
        return toUpdate;
    }

    @Override
    public boolean deleteById(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
