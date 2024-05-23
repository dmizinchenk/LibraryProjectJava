package ru.Zinchenko.LibraryProject.security.entity;

//import jakarta.persistence.*;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.Zinchenko.LibraryProject.models.Book;
import ru.Zinchenko.LibraryProject.models.DTO.UserDTO;

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
    @Column(name = "isblocked")
    private boolean isBloked;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
//
//    public User(String name, String username, String password) {
//        this.name = name;
//        this.username = username;
//        this.password = password;
//    }

    public User() {
//        this.role = Role.ROLE_CUSTOMER;
//        System.out.println("User 43");
    }

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
                dto.getRole()
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
