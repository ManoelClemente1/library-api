package com.manocle.library.service;

import com.manocle.library.exception.BusinessException;
import com.manocle.library.model.entity.Book;
import com.manocle.library.model.repository.BookRepository;
import com.manocle.library.service.impl.BookServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImp(repository);
    }

    @Test
    @DisplayName("deve salvar um livro")
    public void saveBookTest(){

        Book book = createBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        Mockito.when(repository.save(book)).thenReturn(
                Book.builder()
                    .id(1l)
                    .author("Fulano")
                    .title("As aventuras")
                    .isbn("123").build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");

    }



    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn duplicado")
    public void sholdNotSaveABookWithDuplicatedISBN(){

        Book book = createBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado");

        Mockito.verify(repository,Mockito.never()).save(book);


    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest(){
        Long id = 1l;
        Book book = createBook();
        book.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());

    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por id quando nao existe na base")
    public void bookNotFoundByIdTest(){
        Long id = 1l;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        Long id = 1l;
        Book book = createBook();
        book.setId(id);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> repository.delete(book));

        Mockito.verify(repository,Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar um livro inesistente")
    public void deleteInvalidBookTest(){
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.delete(book));

        Mockito.verify(repository, Mockito.never()).delete(book);

    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar um livro inesistente")
    public void updateInvalidBookTest(){
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.update(book));

        Mockito.verify(repository, Mockito.never()).save(book);

    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest(){
        Long id = 1l;

        Book updatingBook = createBook();
        updatingBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updatingBook);

        Book book = service.update(updatingBook);

        assertThat(book.getId()).isEqualTo(updatingBook.getId());
        assertThat(book.getIsbn()).isEqualTo(updatingBook.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(updatingBook.getAuthor());
        assertThat(book.getTitle()).isEqualTo(updatingBook.getTitle());

    }

    private Book createBook() {
        return Book.builder()
                .isbn("123")
                .author("Fulano")
                .title("As aventuras")
                .build();
    }

}
