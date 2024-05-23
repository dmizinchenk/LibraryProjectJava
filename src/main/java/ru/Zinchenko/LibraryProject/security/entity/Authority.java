package ru.Zinchenko.LibraryProject.security.entity;

public enum Authority {
    TOTAL_CONTROL,
    BOOK_HANDLE,
    BOOK_REQUEST;

    public String getAuthority(){
        return toString();
    }
}
