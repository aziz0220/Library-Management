package tn.ensit.miniprojetbibliotheque.models;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Lecteur {

	private static long nbLecteur;
	private final LongProperty CIN;
	private final StringProperty nom;
	private final StringProperty prenom;

//constructeurs

	public Lecteur() {
		this.CIN = new SimpleLongProperty(0);
		this.nom = new SimpleStringProperty("");
		this.prenom = new SimpleStringProperty("");
		nbLecteur++;
	}

	public Lecteur(long CIN, String nom, String prenom) {
		this.CIN = new SimpleLongProperty(CIN);
		this.nom = new SimpleStringProperty(nom);
		this.prenom = new SimpleStringProperty(prenom);
		nbLecteur++;
	}




	//constructeur tp7
	public Lecteur(String ch1) {
		String ch = ch1.replaceAll("\\s+", " ");
		String[] param = ch.split(" ");

		if (param.length >= 3) {
			this.nom = new SimpleStringProperty(param[0]);
			this.prenom = new SimpleStringProperty(param[1]);
			try {
				this.CIN = new SimpleLongProperty(Long.parseLong(param[2].trim()));
			} catch (NumberFormatException e) {
				System.out.println("Erreur !!");
				throw new IllegalArgumentException("Invalid CIN format");
			}
		} else {
			System.out.println("Erreur !!");
			throw new IllegalArgumentException("Invalid input format");
		}
	}


//getters and setters

	public static long getNbLecteur() {
		return nbLecteur;
	}

	public static void setNbLecteur(long nbLecteur) {
		Lecteur.nbLecteur = nbLecteur;
	}

	public long getCIN() {
		return CIN.get();
	}

	public void setCIN(long CIN) {
		this.CIN.set(CIN);
	}

	public String getNom() {
		return nom.get();
	}

	public void setNom(String nom) {
		this.nom.set(nom);
	}

	public String getPrenom() {
		return prenom.get();
	}

	public void setPrenom(String prenom) {
		this.prenom.set(prenom);
	}

	// Affichage
	@Override
	public String toString() {
		return "Lecteur [CIN=" + CIN.get() + ", nom=" + nom.get() + ", prenom=" + prenom.get() + "]";
	}

	// Compare
	public int compare(Lecteur l1) {
		return l1.nom.get().compareToIgnoreCase(nom.get());
	}

	public static int compare(Lecteur l1, Lecteur l2) {
		return l1.nom.get().compareToIgnoreCase(l2.nom.get());
	}

	// The equals method
	@Override
	public int hashCode() {
		return Objects.hash(nom.get());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Lecteur other = (Lecteur) obj;
		return Objects.equals(nom.get(), other.nom.get());
	}
}
