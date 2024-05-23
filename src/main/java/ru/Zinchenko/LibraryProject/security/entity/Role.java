package ru.Zinchenko.LibraryProject.security.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
//    ROLE_ADMIN(Set.of(Authority.TOTAL_CONTROL, Authority.BOOK_HANDLE, Authority.BOOK_REQUEST)),
//    ROLE_EMPLOYEE(Set.of(Authority.BOOK_HANDLE, Authority.BOOK_REQUEST)),
//    ROLE_CUSTOMER(Set.of(Authority.BOOK_REQUEST));
//
//    private final Set<Authority> authorities;
//
//    Role(Set<Authority> authorities){
//        this.authorities = authorities;
//    }
//
//    public Set<SimpleGrantedAuthority> getAuthorities(){
//        return authorities.stream()
//                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
//                .collect(Collectors.toSet());
//    }
    ROLE_ADMIN,
    ROLE_EMPLOYEE,
    ROLE_CUSTOMER;
    public Set<SimpleGrantedAuthority> getAuthorities(){
        return new HashSet<>();
    }
}
