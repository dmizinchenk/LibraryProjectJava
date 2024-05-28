package ru.Zinchenko.LibraryProject.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.Zinchenko.LibraryProject.models.Author;
import ru.Zinchenko.LibraryProject.models.Book;
import ru.Zinchenko.LibraryProject.models.DTO.BookDTO;
import ru.Zinchenko.LibraryProject.models.Order;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.security.securityUser.SecurityUser;
import ru.Zinchenko.LibraryProject.services.implementations.OrderServiceImplementation;
import ru.Zinchenko.LibraryProject.services.implementations.UserServiceImplementation;
import ru.Zinchenko.LibraryProject.services.interfaces.AuthorService;
import ru.Zinchenko.LibraryProject.services.interfaces.BookService;
import ru.Zinchenko.LibraryProject.services.interfaces.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
//        System.out.println("ROLES: ");
//        for (GrantedAuthority role : roles) {
//            System.out.println(role.getAuthority());
//        }
        System.out.println("user role: " + auth.getAuthorities());
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
    public String editbook(@PathVariable int id, Model model){

        // TODO: внедрить список авторов и книгу для формы

        return "ui/pages/bookEdit";
    }
}
