package auca.ac.rw.RestfullApi.controller.library;

import auca.ac.rw.RestfullApi.model.library.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    
    private final  List<Book> books = new ArrayList<>();

    
    public BookController() {
        
        books.add(new Book(1L, "Clean Code", "Robert Martin", 
                          "978-0132350884", 2008));
        books.add(new Book(2L, "The Pragmatic Programmer", "David Thomas", 
                          "978-0201616224", 1999));
        books.add(new Book(3L, "Design Patterns", "Erich Gamma", 
                          "978-0201633610", 1994));
    }

   
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = findBookById(id);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        List<Book> matchingBooks = books.stream()
                .filter(book -> book.getTitle().toLowerCase()
                        .contains(title.toLowerCase()))
                .collect(Collectors.toList());
        
        if (!matchingBooks.isEmpty()) {
            return new ResponseEntity<>(matchingBooks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
       
        Long newId = books.stream()
                .mapToLong(Book::getId)
                .max()
                .orElse(0L) + 1;
        book.setId(newId);
        
        books.add(book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book book = findBookById(id);
        if (book != null) {
            books.remove(book);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    private Book findBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}