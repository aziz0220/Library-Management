package tn.ensit.miniprojetbibliotheque.models;

import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
public class DetailEmprunt {
	private final SimpleLongProperty id;
	private final SimpleLongProperty bookid;
	private final SimpleStringProperty bookname;
	private final SimpleLongProperty lecteurCIN;
	private final SimpleStringProperty lecteurName;
	private final SimpleObjectProperty<Date> dateEmprunt;

	private final ReadOnlyLongWrapper days;
	private final SimpleObjectProperty<Date> dateRetour;

	// Constructor
	public DetailEmprunt(long id, long bookid, String bookname, long lecteurCIN, String lecteurName, Date dateEmprunt, Date dateRetour) {
		this.id = new SimpleLongProperty(id);
		this.bookname = new SimpleStringProperty(bookname);
		this.bookid = new SimpleLongProperty(bookid);
		this.lecteurCIN = new SimpleLongProperty(lecteurCIN);
		this.lecteurName = new SimpleStringProperty(lecteurName);
		this.dateEmprunt = new SimpleObjectProperty<>(dateEmprunt);
		this.dateRetour = new SimpleObjectProperty<>(dateRetour);
		this.days = new ReadOnlyLongWrapper(this, "days", calculateDays(dateEmprunt, dateRetour));
	}

	// Getters and setters
	public long getId() {
		return id.get();
	}

	public SimpleLongProperty idProperty() {
		return id;
	}

	public long getBookid() {
		return bookid.get();
	}

	public SimpleLongProperty bookidProperty() {
		return bookid;
	}

	public String getBookname() {
		return bookname.get();
	}

	public SimpleStringProperty booknameProperty() {
		return bookname;
	}

	public long getLecteurCIN() {
		return lecteurCIN.get();
	}

	public SimpleLongProperty lecteurCINProperty() {
		return lecteurCIN;
	}

	public String getLecteurName() {
		return lecteurName.get();
	}

	public SimpleStringProperty lecteurNameProperty() {
		return lecteurName;
	}

	public Date getDateEmprunt() {
		return dateEmprunt.get();
	}

	public SimpleObjectProperty<Date> dateEmpruntProperty() {
		return dateEmprunt;
	}

	public Date getDateRetour() {
		return dateRetour.get();
	}

	public long getDays() {
		return days.get();
	}

	// Getter for the JavaFX property
	public ReadOnlyLongWrapper daysProperty() {
		return days;
	}

	public SimpleObjectProperty<Date> dateRetourProperty() {
		return dateRetour;
	}


	private long calculateDays(java.util.Date dateEmprunt, java.util.Date dateRetour) {
		if (dateRetour != null) {
			long millisecondsDifference = dateRetour.getTime() - dateEmprunt.getTime();
			return millisecondsDifference / (24 * 60 * 60 * 1000);
		} else {
			long currentMilliseconds = System.currentTimeMillis();
			long millisecondsDifference = currentMilliseconds - dateEmprunt.getTime();
			return millisecondsDifference / (24 * 60 * 60 * 1000);
		}
	}




}