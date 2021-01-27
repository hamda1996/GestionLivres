package com.gestionlivres.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gestionlivres.entities.Books;
import com.gestionlivres.service.BooksService;

@RestController
public class BooksController {

	@Autowired
	public BooksService booksservice;
	
	@GetMapping("/livres")
	public List<Books>getBooks() {
		return booksservice.getBooks();
	}
	
	@GetMapping("/livres/{id}")
	public Books getContact(@PathVariable int id) {
		return booksservice.getBooks(id);
	}
	
	@PostMapping("/livres")
	public Books saveContact(@RequestBody Books book) {
		return booksservice.saveBooks(book);
	}
}
