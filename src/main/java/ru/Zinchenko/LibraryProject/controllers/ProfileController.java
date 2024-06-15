package ru.Zinchenko.LibraryProject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.Zinchenko.LibraryProject.models.Order;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.services.implementations.OrderServiceImplementation;
import ru.Zinchenko.LibraryProject.services.implementations.UserServiceImplementation;
import ru.Zinchenko.LibraryProject.util.OrderDescComparator;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final UserServiceImplementation userService;
    private final OrderServiceImplementation orderService;

    @GetMapping
    public String updateData(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User curUser = userService.findByUsername(auth.getName());
        model.addAttribute("user", curUser);
        return "/ui/pages/userForm";
    }

    @PostMapping
    public String refreshData(@ModelAttribute("user") @Valid User user, RedirectAttributes ra, @RequestParam(value = "newPass", required = false) String newPass, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User curUser = userService.findByUsername(auth.getName());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        if(!curUser.getPassword().equals(encoder.encode(user.getPassword()))) {
            ra.addFlashAttribute("errPass", "Введите верный пароль");
            return "redirect:/profile";
        } else if (newPass == null || newPass.isEmpty()) {
            userService.update(user);
        }
        else {
            userService.updateWithPassword(user);
        }
        return "redirect:/";
    }

    @GetMapping("/currentReserves")
    public String currentReserves(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User curUser = userService.findByUsername(auth.getName());
        List<Order> orders = curUser.getOrders().stream().filter(order -> order.getState().equals(Order.State.RESERVE_NOT_HANDLED)).toList();
        model.addAttribute("head", "Текущие запросы:");
        model.addAttribute("orders", orders);
        model.addAttribute("action", "Отменить");
        model.addAttribute("link", "/profile/deleteOrder/");
        return "/ui/pages/ordersList";
    }

    @GetMapping("/onHandle")
    public String onHandle(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User curUser = userService.findByUsername(auth.getName());
        List<Order> orders = curUser.getOrders().stream().filter(order -> order.getState().equals(Order.State.RESERVE_APPROVE) || order.getState().equals(Order.State.RETURN_DECLINE)).toList();
        model.addAttribute("head", "Книги на руках:");
        model.addAttribute("orders", orders);
        model.addAttribute("action", "Вернуть");
        model.addAttribute("link", "/profile/return/");
        return "/ui/pages/ordersList";
    }

    @PostMapping("/return/{orderId}")
    public String returnBook(@PathVariable("orderId") int id){
        Order order = orderService.findOne(id);
        order.setState(Order.State.RETURN_NOT_HANDLED);
        orderService.update(order);
        return "redirect:/profile/onHandle";
    }

    @GetMapping("/allOrders")
    public String allOrders(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User curUser = userService.findByUsername(auth.getName());
        model.addAttribute("orders", curUser.getOrders());
        return "/ui/pages/allOrdersList";
    }

    @PostMapping("/deleteOrder/{id}")
    public String delOrder(@PathVariable("id") int id){
        Order order = orderService.findOne(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User curUser = userService.findByUsername(auth.getName());
        curUser.getOrders().remove(order);
        userService.update(curUser);

        orderService.deleteById(id);
        return "redirect:/profile/currentOrders";
    }
}
