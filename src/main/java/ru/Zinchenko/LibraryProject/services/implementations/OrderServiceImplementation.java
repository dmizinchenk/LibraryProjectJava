package ru.Zinchenko.LibraryProject.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Zinchenko.LibraryProject.models.Order;
import ru.Zinchenko.LibraryProject.models.repositaries.OrderRepository;
import ru.Zinchenko.LibraryProject.services.interfaces.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImplementation implements OrderService {
    private OrderRepository repository;

    @Autowired
    public OrderServiceImplementation(OrderRepository repository){
        this.repository = repository;
    }
    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Order findOne(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    public Order update(Order order) {
        if (!repository.existsById(order.getId())) {
            return null;
        }
        Order toUpdate = repository.findById(order.getId()).get();
        toUpdate.copy(order);
        repository.save(toUpdate);
        return toUpdate;
    }

    @Override
    public Optional<Order> findLastOrderByBookAndUser(int userId, int bookId){
        return repository.findLastOrderByUserIdAndBookId(userId, bookId);
    }

    @Override
    public List<Order> getAllRequestToReserve() {
        return repository.findOrderByState(Order.State.RESERVE_NOT_HANDLED);
    }

    @Override
    public List<Order> getAllRequestToReturn() {
        return repository.findOrderByState(Order.State.RETURN_NOT_HANDLED);
    }

    @Override
    public boolean deleteById(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
