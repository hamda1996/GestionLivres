package com.gestionlivres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gestionlivres.entities.Autors;
import com.gestionlivres.service.AutorsService;

@RestController
public class AutorsController {

	@Autowired
	private AutorsService autorsservice;
	
	@GetMapping("/auteurs")
	public List<Autors>getBooks() {
		return autorsservice.getAutors();
	}
	
	@GetMapping("/auteurs/{id}")
	public Autors getAutor(@PathVariable int id) {
		return autorsservice.getAutor(id);
	}
	
	@PostMapping("/auteurs")
	public Autors addAutor(@RequestBody Autors autor) {
		return autorsservice.addAutor(autor);
	}
	
	@PutMapping("/Auteurs")
	public Autors updateAutor(@RequestBody Autors autor) {
		return autorsservice.updateAutor(autor);
	}	
	@DeleteMapping("/Auteurs/{id}")
	public void deleteAutor(@PathVariable int id) {
		autorsservice.deleteAutor(id);
	}
}
