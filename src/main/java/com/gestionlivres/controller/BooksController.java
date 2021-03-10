package com.gestionlivres.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestionlivres.entities.Books;
import com.gestionlivres.service.BooksService;

@RestController
public class BooksController {

	@Autowired
	private BooksService booksservice;
	
	@GetMapping("/livres")
	public List<Books>getBooks() {
		return booksservice.getBooks();
	}
	
	@GetMapping("/livres/{id}")
	public Books getBook(@PathVariable int id) {
		return booksservice.getBook(id);
	}
	
	@GetMapping("/livresOf/{autor}")
	public List<Books> getBooksByAutor(@PathVariable String autor) {
		return booksservice.getBooksByAutor(autor);
	}
	
	@PostMapping("/livres")
	public Books addBook(@RequestBody Books book) {
		return booksservice.addBook(book);
	}
	
	@PutMapping("/livres")
	public Books updateBook(@RequestBody Books book) {
		return booksservice.updateBook(book);
	}	
	@DeleteMapping("/livres/{id}")
	public void deleteBook(@PathVariable int id) {
		booksservice.deleteBook(id);
	}
	
	@GetMapping("/livre")
	public Books updateBookFromId(@RequestParam(name = "bookId") int bookId, @RequestParam(name = "stockToRemove") int stockToRemove) {
		Books book = booksservice.updateByBookId(bookId, stockToRemove);
		return book;
	}
	
	
}
