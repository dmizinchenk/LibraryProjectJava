package ru.Zinchenko.LibraryProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.Zinchenko.LibraryProject.security.entity.User;

import javax.persistence.*;
import java.util.Comparator;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state = State.RESERVE_NOT_HANDLED;
    @ManyToOne()
    @JoinColumn(name = "reserved_by")
    private User reserved_by;
    @ManyToOne()
    @JoinColumn(name = "returned_by")
    private User returned_by;

    public void copy(Order order) {
        book = order.getBook();
        user = order.getUser();
        state = order.getState();
    }

    public enum State{
        RESERVE_NOT_HANDLED,
        RESERVE_APPROVE,
        RESERVE_DECLINE,
        RETURN_NOT_HANDLED,
        RETURN_APPROVE,
        RETURN_DECLINE
    }
}
