package com.gestionlivres.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestionlivres.entities.Autors;

@Repository
public interface AutorsRepository extends JpaRepository<Autors, Integer> {

	public Autors findByid(int id);
}
