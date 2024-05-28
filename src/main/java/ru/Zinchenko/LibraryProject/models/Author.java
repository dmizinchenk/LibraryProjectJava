package ru.Zinchenko.LibraryProject.models;

import javax.persistence.*;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "middlename")
    private String middleName;
    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();

    public String getShortInfo() {

        StringBuilder builder = new StringBuilder();
        builder.append(name.charAt(0));
        builder.append('.');
        if(middleName != null && !middleName.isEmpty()){
            builder.append(' ');
            builder.append(middleName.charAt(0));
            builder.append('.');
        }
        builder.append(' ');
        builder.append(surname);
        return builder.toString();
    }

    public String getFullName(){
        return surname + " " + name + ((middleName == null || middleName.isEmpty()) ? "" : (" " + middleName));
    }
}
