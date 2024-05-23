package ru.Zinchenko.LibraryProject.services.interfaces;

import javax.transaction.Transactional;

import java.util.List;

public interface DAO <T>{
    List<T> findAll();
    T findOne(int id);
    T save(T t);
    @Transactional
    T update(T t);
    boolean deleteById(int id);
}
