package tn.ensit.miniprojetbibliotheque.models;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Livre {
	private static long nbLivre;
	private final LongProperty code;
	private final StringProperty titre;
	private final StringProperty auteur;
	private final LongProperty ISBN;

	// Constructors
	public Livre(long code, String titre, String auteur) {
		this.titre = new SimpleStringProperty(titre);
		this.auteur = new SimpleStringProperty(auteur);
		nbLivre++;
		this.code = new SimpleLongProperty(code);
		this.ISBN = new SimpleLongProperty(0);
	}

	public Livre(long code, String titre, String auteur, long ISBN) {
		this.titre = new SimpleStringProperty(titre);
		this.auteur = new SimpleStringProperty(auteur);
		this.ISBN = new SimpleLongProperty(ISBN);
		nbLivre++;
		this.code = new SimpleLongProperty(code);
	}


	public Livre(String titre, String auteur, long ISBN) {
		this.titre = new SimpleStringProperty(titre);
		this.auteur = new SimpleStringProperty(auteur);
		this.ISBN = new SimpleLongProperty(ISBN);
		nbLivre++;
		this.code = new SimpleLongProperty(nbLivre);
	}

	public Livre() {
		nbLivre++;
		this.code = new SimpleLongProperty(nbLivre);
		this.titre = new SimpleStringProperty("");
		this.auteur = new SimpleStringProperty("");
		this.ISBN = new SimpleLongProperty(0);
	}

	// Getters and Setters
	public static long getNbLivre() {
		return nbLivre;
	}

	public static void setNbLivre(long nbLivre) {
		Livre.nbLivre = nbLivre;
	}

	public String getTitre() {
		return titre.get();
	}

	public long getCode() {
		return code.get();
	}

	public void setTitre(String titre) {
		this.titre.set(titre);
	}

	public String getAuteur() {
		return auteur.get();
	}

	public void setAuteur(String auteur) {
		this.auteur.set(auteur);
	}

	public long getISBN() {
		return ISBN.get();
	}

	public void setISBN(long ISBN) {
		this.ISBN.set(ISBN);
	}

	// Affichage
	@Override
	public String toString() {
		return "Livre [code=" + code.get() + ", titre=" + titre.get() + ", auteur=" + auteur.get() + "]";
	}

	// Compare
	public int compare(Livre l1) {
		return l1.titre.get().compareToIgnoreCase(titre.get());
	}

	public static int compare(Livre l1, Livre l2) {
		return l1.titre.get().compareToIgnoreCase(l2.titre.get());
	}

	// La méthode equals
	@Override
	public int hashCode() {
		return Objects.hash(auteur.get());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Livre other = (Livre) obj;
		return Objects.equals(auteur.get(), other.auteur.get());
	}

	public void setCode(long code) {
		this.code.set(code);

	}
}
