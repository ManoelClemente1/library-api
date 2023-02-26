package com.manocle.library.service.impl;

import com.manocle.library.model.entity.Book;
import com.manocle.library.model.repository.BookRepository;
import com.manocle.library.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImp implements BookService {

    private BookRepository repository;

    public BookServiceImp(BookRepository repository) {
        this.repository = repository;
    }


    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
}
