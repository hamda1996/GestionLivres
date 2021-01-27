package com.gestionlivres.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Books {
	@Id @GeneratedValue
	private int id;
	private String nom;
	private String auteur;
	@Temporal(TemporalType.DATE)
	private Date datePublication;
	
	public Books() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Books(int id, String nom, String auteur, Date datePublication) {
		super();
		this.id = id;
		this.nom = nom;
		this.auteur = auteur;
		this.datePublication = datePublication;
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
	public String getAuteur() {
		return auteur;
	}
	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}
	public Date getDatePublication() {
		return datePublication;
	}
	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}
}
