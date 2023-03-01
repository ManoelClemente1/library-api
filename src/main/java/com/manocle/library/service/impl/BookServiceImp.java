package com.manocle.library.service.impl;

import com.manocle.library.exception.BusinessException;
import com.manocle.library.model.entity.Book;
import com.manocle.library.model.repository.BookRepository;
import com.manocle.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImp implements BookService {

    private BookRepository repository;

    public BookServiceImp(BookRepository repository) {
        this.repository = repository;
    }


    @Override
    public Book save(Book book) {

        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("Isbn já cadastrado");
        }

        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Book book) {

    }

    @Override
    public Book update(Book book) {
        return null;
    }
}
