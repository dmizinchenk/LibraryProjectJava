package ru.Zinchenko.LibraryProject.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BookDTO {
    private int id;
    private List<String> authors = new ArrayList<>();
//    private String authors;
    private String title;
    private String annotation;
}
