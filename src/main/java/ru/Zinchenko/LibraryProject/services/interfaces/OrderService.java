package ru.Zinchenko.LibraryProject.services.interfaces;

import org.springframework.stereotype.Service;
import ru.Zinchenko.LibraryProject.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService extends DAO<Order> {
    Optional<Order> findLastOrderByBookAndUser(int userId, int bookId);
    List<Order> getAllRequestToReserve();
    List<Order> getAllRequestToReturn();
}
