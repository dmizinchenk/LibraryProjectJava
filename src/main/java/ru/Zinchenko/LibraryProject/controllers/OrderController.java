package ru.Zinchenko.LibraryProject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Zinchenko.LibraryProject.models.Book;
import ru.Zinchenko.LibraryProject.models.Order;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.services.implementations.UserServiceImplementation;
import ru.Zinchenko.LibraryProject.services.interfaces.BookService;
import ru.Zinchenko.LibraryProject.services.interfaces.OrderService;

import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final BookService bookService;
    private final UserServiceImplementation userService;

    @Autowired
    public OrderController(OrderService orderService, BookService bookService, UserServiceImplementation userService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/reserves")
    public String requestsToReserve(Model model){
        model.addAttribute("requests", orderService.getAllRequestToReserve());
        model.addAttribute("action", "/order/reserves");
        return "ui/pages/requests";
    }
    @GetMapping("/returns")
    public String requestsToReturn(Model model){
        model.addAttribute("requests", orderService.getAllRequestToReturn());
        model.addAttribute("action", "/order/returns");
        return "ui/pages/requests";
    }

    @PostMapping("/reserves/approve/{id}")
    public String reservesApprove(@PathVariable int id){
        Order order = orderService.findOne(id);

        User user = order.getUser();
        user.getBooks().add(order.getBook());
        userService.update(user);

//        order.setHaveOwner(true);
        order.setHandled(true);
        orderService.update(order);
        return "redirect:/order/reserves";

    }
    @PostMapping("/reserves/decline/{id}")
    public String reservesDecline(@PathVariable int id){
        Order order = orderService.findOne(id);
        User user = order.getUser();
        user.getOrders().remove(order);
        userService.update(user);

        order.setUser(null);
        order.setHaveOwner(false);
        order.setHandled(true);
        orderService.update(order);
        return "redirect:/order/reserves";
    }
    @PostMapping("/returns/approve/{id}")
    public String returnsApprove(@PathVariable int id){
        Order order = orderService.findOne(id);

        User user = order.getUser();
        user.getBooks().remove(order.getBook());
        user.getOrders().remove(order);
        userService.update(user);

        order.setUser(null);
        order.setHandled(true);

        orderService.update(order);
        return "redirect:/order/returns";
    }
    @PostMapping("/returns/decline/{id}")
    public String returnsDecline(@PathVariable int id){
        Order order = orderService.findOne(id);
        order.setHaveOwner(true);
        order.setHandled(true);
        orderService.update(order);
        return "redirect:/order/returns";
    }

    @PostMapping(value = "/getBook/{bookId}")
    public String reserveBook(@PathVariable int bookId){
        Book book = bookService.findOne(bookId);
        User curUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Order order = new Order();
        order.setUser(curUser);
        order.setBook(book);
        order.setHandled(false);
//        order.setHaveOwner(false);
        order.setHaveOwner(true);
        orderService.save(order);

        curUser.getOrders().add(order);
        userService.update(curUser);

        return "redirect:/book/" + bookId;
    }
    @PostMapping(value = "/returnBook/{bookId}")
    public String returnBook(@PathVariable int bookId){
        User curUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Optional<Order> order = orderService.findLastOrderByBookAndUser(curUser.getId(), bookId);
        if(order.isPresent()) {
            Order temp = order.get();
            temp.setHandled(false);
            temp.setHaveOwner(false);
            orderService.update(temp);

        }
        return "redirect:/book/" + bookId;
    }
}
