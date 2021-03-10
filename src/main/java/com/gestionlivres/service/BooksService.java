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
	
	public Books getBook(@PathVariable int id) {
		return booksrepository.findByid(id);
	}
	
	public Books addBook(@RequestBody Books book) {
		return booksrepository.save(book);
	}
	
	public Books updateBook(@RequestBody Books book) {
		return booksrepository.save(book);
	}	
	
	public void deleteBook(@PathVariable int id) {
		booksrepository.deleteById(id);
	}
	
	public List<Books> getBooksByAutor(@PathVariable String autor){
		return booksrepository.findByAutorNom(autor);
	}

	public Books updateByBookId(int bookId, int stockToRemove) {
		return booksrepository.findByIdAndStock(bookId, stockToRemove);
	}
}
