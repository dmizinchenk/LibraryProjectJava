package ru.Zinchenko.LibraryProject.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.Zinchenko.LibraryProject.models.Author;
import ru.Zinchenko.LibraryProject.models.Book;
import ru.Zinchenko.LibraryProject.models.DTO.BookDTO;
import ru.Zinchenko.LibraryProject.models.Order;
import ru.Zinchenko.LibraryProject.models.Review;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.services.implementations.UserServiceImplementation;
import ru.Zinchenko.LibraryProject.services.interfaces.AuthorService;
import ru.Zinchenko.LibraryProject.services.interfaces.BookService;
import ru.Zinchenko.LibraryProject.services.interfaces.ReviewService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class LibraryController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final UserServiceImplementation userService;
    private final ReviewService reviewService;

    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }
    @GetMapping("/")
    public String home(@RequestParam(name = "search", required = false) String find, @RequestParam(name = "page", required = false) String page, Model model){

        int currentPage = (page == null || page.isEmpty()) ? 0 : Integer.parseInt(page);
        List<Book> books;

        if(find == null || find.isEmpty()){

            books = bookService.findAll(currentPage).getContent();
        }
        else{
            books = bookService.findBook(find);
            books.addAll(bookService.findByAuthor(find));
        }

        List<BookDTO> booksDTO = books.stream()
                .map(b -> BookDTO.builder()
                        .id(b.getId())
                        .authors(b.getAuthors().stream()
                                    .map(Author::getShortInfo)
                                    .collect(Collectors.toList()))
                        .title(b.getTitle())
                        .annotation(b.getAnnotation())
                        .pathToFile(b.getPathToFile())
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("books", booksDTO);
        model.addAttribute("page", currentPage);

        return "ui/pages/index";
    }


    @GetMapping(value = "/book/{bookId}")
    public String categoryCatalogue(@PathVariable int bookId, Model model) {
        Book book = bookService.findOne(bookId);
        BookDTO dto = new BookDTO(book.getId(),
                book.getAuthors().stream().map(Author::getFullName).collect(Collectors.toList()),
                book.getTitle(),
                book.getAnnotation(),
                book.getBooksCount(),
                book.getPathToFile());
        model.addAttribute("book", dto);

        User curUser = getCurrentUser();

        List<Order> orders = curUser.getOrders().stream()
                .filter(order -> order.getBook().equals(book))
                .toList();
        boolean canGet = orders.isEmpty() || orders.stream().filter(order -> !order.getState().equals(Order.State.RETURN_APPROVE) && !order.getState().equals(Order.State.RESERVE_DECLINE)).toList().isEmpty();
        boolean canReturn = !orders.stream().filter(order -> order.getState().equals(Order.State.RESERVE_APPROVE) || order.getState().equals(Order.State.RETURN_DECLINE)).toList().isEmpty();

        Review review = curUser.getReviews().stream().filter(r -> r.getBook().equals(book)).findFirst().orElse(null);
        if (review == null || !review.isFavorite()){
            model.addAttribute("favorite", false);
        }
        else {
            model.addAttribute("favorite", true);
        }

        List<Integer> marks = reviewService.getMarks(bookId);
        if(marks.isEmpty()){
            model.addAttribute("rating", '-');
        }
        else {
            double sum = marks.stream()
                    .mapToInt(Integer::intValue)
                    .average().orElse(0);
            model.addAttribute("rating", String.format("%.1f",sum));
        }

        model.addAttribute("isCanGet", canGet && book.getBooksCount() > 0);
        model.addAttribute("isCanReturn", canReturn);
        return "ui/pages/detail";
    }

    @GetMapping("/book/{id}/edit")
    public String editBook(@PathVariable int id, Model model){

        Book book = bookService.findOne(id);
        List<Author> authors = authorService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("authorsList", authors);
        model.addAttribute("action", "/" + id + "/edit");
        return "ui/pages/bookEdit";
    }

    @PostMapping("/book/{id}/edit")
    public String modifyBook(@ModelAttribute("book") Book book, @RequestParam("cover") MultipartFile file) {

        try{
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isBlank()){
                String ext = fileName.substring(fileName.lastIndexOf("."));
                Path path = Paths.get("D:\\Обучение Шаг\\Java\\LibraryProject\\src\\main\\resources\\static\\img\\Books\\" + book.getId() + ext);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                book.setPathToFile("/img/Books/" + book.getId() + ext);
            }

            bookService.update(book);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return "redirect:/book/" + book.getId();
    }
    @GetMapping("/book/create")
    public String newBook(@ModelAttribute("book") Book book, Model model){

        List<Author> authors = authorService.findAll();

        model.addAttribute("authorsList", authors);
        model.addAttribute("action", "/create");
        return "ui/pages/bookEdit";
    }

    @PostMapping("/book/create")
    public String createBook(@ModelAttribute("book") Book book, @RequestParam("cover") MultipartFile file) {

        try{
            book = bookService.save(book);
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isBlank()){
                String ext = fileName.substring(fileName.lastIndexOf("."));
                Path path = Paths.get("D:\\Обучение Шаг\\Java\\LibraryProject\\src\\main\\resources\\static\\img\\Books\\" + book.getId() + ext);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                book.setPathToFile("/img/Books/" + book.getId() + ext);
                bookService.update(book);
            }
            return "redirect:/book/" + book.getId();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/book/{id}/delete")
    public String deleteBook(@PathVariable int id){
        bookService.deleteById(id);
        return "redirect:/";
    }

    @PostMapping(value = "/book/{id}/addToFav")
    public String toFavorite(@PathVariable int id){
        User curUser = getCurrentUser();

        Optional<Review> reviewFromDB = reviewService.getReviewByUserAndBookId(curUser, id);
        Review review;
        if(reviewFromDB.isPresent()){
            review = reviewFromDB.get();
        }
        else {
            Book book = bookService.findOne(id);
            review = new Review();
            review.setUser(curUser);
            review.setBook(book);
        }
        review.setFavorite(true);
        review = reviewService.save(review);

        if(reviewFromDB.isEmpty()){
            curUser.getReviews().add(review);
            userService.update(curUser);
        }

        return "redirect:/book/" + id;
    }
    @PostMapping(value = "/book/{id}/removeFromFav")
    public String fromFavorite(@PathVariable int id){
        User curUser = getCurrentUser();
        Review review = curUser.getReviews().stream().filter(r -> r.getBook().getId().equals(id)).findFirst().orElse(null);
        if(review != null){
            review.setFavorite(false);
            reviewService.update(review);
        }
        return "redirect:/book/" + id;
    }
}
