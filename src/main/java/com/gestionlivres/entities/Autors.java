package com.gestionlivres.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Autors {

	@Id @GeneratedValue
	private int id;
	private String nom;
	private String prenom;
	@Temporal(TemporalType.DATE)
	private Date dateNaissance;
	//private List<Books>livres;
	
	public Autors() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	/*public List<Books> getLivres() {
		return livres;
	}

	public void setLivres(List<Books> livres) {
		this.livres = livres;
	}*/

	public Autors(int id, String nom, String prenom, Date dateNaissance) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
		//this.livres = livres;
	}

	
}
