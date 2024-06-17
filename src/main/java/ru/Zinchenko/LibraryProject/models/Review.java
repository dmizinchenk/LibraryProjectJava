package ru.Zinchenko.LibraryProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.Zinchenko.LibraryProject.security.entity.User;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Review")
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "bookid", referencedColumnName = "id")
    private Book book;
    @ManyToOne()
    @JoinColumn(name = "userid")
    private User user;
    @Column(name = "is_favorite")
    private boolean favorite;
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
    @Column(name = "mark")
    private int mark;

    public void copy(Review review) {
        this.id = review.getId();
        this.book = review.getBook();
        this.user = review.getUser();
        this.favorite = review.isFavorite();
        this.comment = review.getComment();
        this.mark = review.getMark();
    }
}
