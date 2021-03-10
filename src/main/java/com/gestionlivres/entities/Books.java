package com.gestionlivres.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Books {
	@Id @GeneratedValue
	private long id;
	private String nom;
	private float prix;
	@Temporal(TemporalType.DATE)
	private Date datePublication;
	private int stock;
	@ManyToOne
	private Autors autor;	
	
	
	public Books() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Books(long id, String nom, float prix, Date datePublication, int stock, Autors autor) {
		super();
		this.id = id;
		this.nom = nom;
		this.prix = prix;
		this.datePublication = datePublication;
		this.stock = stock;
		this.autor = autor;
	}

	@Override
	public String toString() {
		return "Books [id=" + id + ", nom=" + nom + ", prix=" + prix + ", datePublication=" + datePublication + ", stock=" + stock + "; autor=" + autor + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public Date getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}
	
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Autors getAutor() {
		return autor;
	}

	public void setAutor(Autors autor) {
		this.autor = autor;
	}
	

}
