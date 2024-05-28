package ru.Zinchenko.LibraryProject.security.entity;

//import jakarta.persistence.*;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.Zinchenko.LibraryProject.models.Book;
import ru.Zinchenko.LibraryProject.models.DTO.UserDTO;
import ru.Zinchenko.LibraryProject.models.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
//@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "username")
    private String name;
    @Column(name = "email")
    private String username;
    @Column(name = "is_blocked")
    private boolean isBloked;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(){{add(new SimpleGrantedAuthority(role.name()));}};
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User getInstanceFromDTO(UserDTO dto){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getUsername(),
                dto.isBloked(),
                encoder.encode(dto.getPassword()),
                new ArrayList<>(),
                dto.getRole(),
                new ArrayList<>()
                );
    }

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
