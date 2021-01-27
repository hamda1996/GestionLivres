package com.gestionlivres.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.gestionlivres.dao.BooksRepository;
import com.gestionlivres.entities.Books;

@Service
public class BooksService {

	@Autowired
	private BooksRepository booksrepository;
	
	public List<Books>getBooks() {
		return booksrepository.findAll();
	}
	
	public Books getBooks(@PathVariable int id) {
		return booksrepository.findByid(id);
	}
	
	public Books saveBooks(@RequestBody Books contact) {
		return booksrepository.save(contact);
	}
}
