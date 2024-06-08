package ru.Zinchenko.LibraryProject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.Zinchenko.LibraryProject.models.Author;
import ru.Zinchenko.LibraryProject.services.implementations.AuthorServiceImplementation;


@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorServiceImplementation authorService;

    @GetMapping()
    public String listAuthors(Model model){
        model.addAttribute("authors", authorService.findAll());
        return "/ui/pages/authorsList";
    }

    @GetMapping("/create")
    public String newAuthor(@ModelAttribute("author") Author author, Model model){
        model.addAttribute("action", "/create");
        return "ui/pages/authorForm";
    }

    @PostMapping("/create")
    public String createAuthor(@ModelAttribute("author") Author author) {
        authorService.save(author);
        return "redirect:/authors";
    }

    @GetMapping("/{id}/edit")
    public String editAuthor(@PathVariable int id, Model model){
        Author author = authorService.findOne(id);
        model.addAttribute("author", author);
        model.addAttribute("action", "/" + id + "/edit");
        return "ui/pages/authorForm";
    }

    @PostMapping("/{id}/edit")
    public String modifyAuthor(@ModelAttribute("author") Author author) {
        authorService.update(author);
        return "redirect:/authors";
    }
}
