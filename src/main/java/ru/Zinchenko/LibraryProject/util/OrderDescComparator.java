package ru.Zinchenko.LibraryProject.util;

import ru.Zinchenko.LibraryProject.models.Order;

import java.util.Comparator;

public class OrderDescComparator implements Comparator<Order> {

    public int compare(Order a, Order b){

        return a.getId().compareTo(b.getId());
    }
}