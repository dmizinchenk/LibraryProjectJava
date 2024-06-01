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
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.services.implementations.OrderServiceImplementation;
import ru.Zinchenko.LibraryProject.services.implementations.UserServiceImplementation;
import ru.Zinchenko.LibraryProject.services.interfaces.AuthorService;
import ru.Zinchenko.LibraryProject.services.interfaces.BookService;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
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
    private final OrderServiceImplementation orderService;

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
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("books", booksDTO);
        model.addAttribute("page", currentPage);

        return "ui/pages/index";
    }


    @GetMapping(value = "/book/{id}")
    public String categoryCatalogue(@PathVariable int id, Model model) {
        Book book = bookService.findOne(id);
        BookDTO dto = new BookDTO(book.getId(), book.getAuthors().stream().map(Author::getFullName).collect(Collectors.toList()), book.getTitle(), book.getAnnotation());
        model.addAttribute("book", dto);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User curUser = userService.findByUsername(auth.getName());
        Optional<Order> order = orderService.findLastOrderByBookAndUser(curUser.getId(), id);
        boolean isHaveBook = false;
        boolean isCanReturn = false;
        if(order.isPresent()){
            Order o = order.get();
            if(o.getBook().equals(book)){
                isHaveBook = true;
            }
            if(o.getBook().equals(book) && o.isHaveOwner() && o.isHandled()){
                isCanReturn = true;
            }
        }
        model.addAttribute("isCanGet", !isHaveBook);
        model.addAttribute("isCanReturn", isCanReturn);
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
                Path path = Paths.get("D:\\Обучение Шаг\\Java\\LibraryProject\\src\\main\\resources\\static\\img\\Books\\" + book.getTitle() + ext);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        bookService.update(book);
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
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isBlank()){
                String ext = fileName.substring(fileName.lastIndexOf("."));
                Path path = Paths.get("D:\\Обучение Шаг\\Java\\LibraryProject\\src\\main\\resources\\static\\img\\Books\\" + book.getTitle() + ext);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return "redirect:/book/" + bookService.save(book).getId();
    }

    @PostMapping(value = "/book/{id}/delete")
    public String deleteBook(@PathVariable int id){
        bookService.deleteById(id);
        return "redirect:/";
    }
}
