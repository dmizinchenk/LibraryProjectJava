package ru.Zinchenko.LibraryProject.security.securityUser;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.Zinchenko.LibraryProject.security.entity.User;

import java.util.HashSet;
import java.util.Set;

@Data
public class SecurityUser implements UserDetails {
    private final User user;
//    private final Set<SimpleGrantedAuthority> authorities;

    public SecurityUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return new HashSet<>(){{add(new SimpleGrantedAuthority(user.getRole().toString()));}};
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isBloked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    public static UserDetails fromEntity(User user) {
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                true,
//                true,
//                true,
//                true,
//                user.getRole().getAuthorities()
//        );
//    }
}
