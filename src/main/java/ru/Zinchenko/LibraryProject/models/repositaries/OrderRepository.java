package ru.Zinchenko.LibraryProject.models.repositaries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.Zinchenko.LibraryProject.models.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = """
            SELECT Orders.*
            FROM Orders
            WHERE Orders.userid = :userId AND Orders.bookid = :bookId
            ORDER BY Orders.id DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Order> findLastOrderByUserIdAndBookId(@Param("userId") int userId, @Param("bookId") int bookId);

    List<Order> findOrderByState(Order.State state);
//    @Query(value = """
//            SELECT Orders.*
//            FROM Orders
//            WHERE Orders.is_handled = :isHandled AND Orders.have_owner = :haveOwner
//            """, nativeQuery = true)
//    List<Order> findOrderByHandledAndHaveOwner(@Param("isHandled") boolean isHandled, @Param("haveOwner") boolean haveOwner);
}
