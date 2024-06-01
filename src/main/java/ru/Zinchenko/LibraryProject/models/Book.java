package ru.Zinchenko.LibraryProject.models;

import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.Zinchenko.LibraryProject.security.entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "authorsbooks", joinColumns = @JoinColumn(name = "bookid"), inverseJoinColumns = @JoinColumn(name = "authorid"))
    private List<Author> authors = new ArrayList<>();

    @Column(name = "title")
    private String title;

    @Column(name = "annotation", columnDefinition = "TEXT")
    private String annotation;

//    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User owner;

    public void copy(Book source){
        authors = source.getAuthors();
        title = source.getTitle();
        annotation = source.getAnnotation();
//        owner = source.getOwner();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", annotation='" + annotation + '\'' +
                '}';
    }
}
