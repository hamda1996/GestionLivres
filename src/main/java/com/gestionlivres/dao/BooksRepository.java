package com.gestionlivres.dao;

import com.gestionlivres.entities.Books;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {
	
	public Books findByid(int id);
	
	@Query("SELECT * FROM Books WHERE Books.Autors.nom=autor") //obtenir la liste de livres d'un auteur
	public List<Books> findByAutorNom(String autor);

	//@Modifying
	@Query("UPDATE Books SET Books.stock= 10 WHERE Books.id= 1")
	public Books findByIdAndStock(@Param("bookId") int bookId,@Param("stockToRemove") int stockToRemove); 

}
