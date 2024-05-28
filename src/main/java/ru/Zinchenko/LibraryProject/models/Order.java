package ru.Zinchenko.LibraryProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.Zinchenko.LibraryProject.security.entity.User;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "bookid", referencedColumnName = "id")
    private Book book;
    @ManyToOne()
    @JoinColumn(name = "userid")
    private User user;
    @Column(name = "is_handled")
    private boolean isHandled;
    @Column(name = "have_owner")
    private boolean haveOwner;

    public void copy(Order order) {
        book = order.getBook();
        user = order.getUser();
        isHandled = order.isHandled();
        haveOwner = order.isHaveOwner();
    }
}
