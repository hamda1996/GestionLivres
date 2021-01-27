package com.gestionlivres.dao;

import com.gestionlivres.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {
	
	public Books findByid(int id);

}
