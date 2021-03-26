package com.gestionlivres.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.gestionlivres.dao.BooksRepository;
import com.gestionlivres.entities.Books;
import com.gestionlivres.entities.StockDto;

@Service
public class BooksService {

	@Autowired
	private BooksRepository booksrepository;
	
	public List<Books>getBooks() {
		return booksrepository.findAll();
	}
	
	public Books getBook(@PathVariable long id) {
		return booksrepository.findByid(id);
	}
		
	public Books addBook(@RequestBody Books book) {
		return booksrepository.save(book);
	}
	
	public Books updateBook(@RequestBody Books book) {
		return booksrepository.save(book);
	}	
	
	public void deleteBook(@PathVariable long id) {
		booksrepository.deleteById(id);
	}
	
	public List<Books> getBooksByAutor(@PathVariable String autor){
		return booksrepository.findByAutorNom(autor);
	}

	public Books manageBookStock(StockDto stockDto) {
		Books books = null;
		if(stockDto != null) {
			books = booksrepository.findByid(stockDto.getProductId());
			if(books != null && books.getStock() - stockDto.getQuantityToReduce() > 0) {
				books.setStock(books.getStock() - stockDto.getQuantityToReduce());
				books = booksrepository.save(books);
			}
		}
		return books;
	}

}
