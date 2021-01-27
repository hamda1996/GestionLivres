package com.gestionlivres.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.gestionlivres.dao.AutorsRepository;
import com.gestionlivres.entities.Autors;

@Service
public class AutorsService {

	@Autowired
	private AutorsRepository autorsrepository;
	
	public List<Autors>getAutors() {
		return autorsrepository.findAll();
	}
	
	public Autors getAutor(@PathVariable int id) {
		return autorsrepository.findByid(id);
	}
	
	public Autors addAutor(@RequestBody Autors autor) {
		return autorsrepository.save(autor);
	}
	
	public Autors updateAutor(@RequestBody Autors autor) {
		return autorsrepository.save(autor);
	}	
	
	public void deleteAutor (@PathVariable int id) {
		autorsrepository.deleteById(id);
	}
	
}
