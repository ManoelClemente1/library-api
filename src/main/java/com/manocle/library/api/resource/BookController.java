package com.manocle.library.api.resource;

import com.manocle.library.api.dto.BookDTO;
import com.manocle.library.api.exceptions.handler.ApiErrors;
import com.manocle.library.exception.BusinessException;
import com.manocle.library.model.entity.Book;
import com.manocle.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto){
        Book entity = modelMapper.map(dto,Book.class);

        entity = service.save(entity);

        return modelMapper.map(entity,BookDTO.class);
    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable Long id){

        return service
                .getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handlerValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException){
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        return new ApiErrors(bindingResult);

    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException businessException){
        return new ApiErrors(businessException);
    }

}
