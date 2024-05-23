package ru.Zinchenko.LibraryProject.models.DTO;

import lombok.Data;
import ru.Zinchenko.LibraryProject.models.Book;
import ru.Zinchenko.LibraryProject.security.entity.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    public int id;
    private String name;
    private String username;
    private boolean isBloked;
    private String password;
    private Role role;

    public boolean getIsBloked() {
        return isBloked;
    }

    public void setIsBloked(boolean bloked) {
        isBloked = bloked;
    }
    public boolean isBloked() {
        return isBloked;
    }

    public void setBloked(boolean bloked) {
        isBloked = bloked;
    }
}
