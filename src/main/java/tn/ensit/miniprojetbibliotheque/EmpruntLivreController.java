package tn.ensit.miniprojetbibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import tn.ensit.miniprojetbibliotheque.alert.AlertMaker;
import tn.ensit.miniprojetbibliotheque.callback.BookReturnCallback;
import tn.ensit.miniprojetbibliotheque.database.EmpruntController;
import tn.ensit.miniprojetbibliotheque.database.LivreController;
import tn.ensit.miniprojetbibliotheque.database.LecteurController;
import tn.ensit.miniprojetbibliotheque.models.DetailEmprunt;
import tn.ensit.miniprojetbibliotheque.models.Lecteur;
import tn.ensit.miniprojetbibliotheque.models.Livre;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;

import java.util.stream.Collectors;

public class EmpruntLivreController {

    @FXML
    private ComboBox<String> booksComboBox;

    @FXML
    private ComboBox<String> readersComboBox;

    @FXML
    private ComboBox<String> borrowedBooksComboBox;

    @FXML
    private Button empruntlist;

    @FXML
    private Button lecteursliste;

    @FXML
    private Button livrelistes;

    private ObservableList<Livre> allBooksList;
    private ObservableList<Lecteur> allReadersList;
    private ObservableList<DetailEmprunt> borrowedBooksList;
    private ObservableList<DetailEmprunt> returningBooksList; // Declare ReturningBooksList

    @FXML
    public void initialize() {
        // Initialize combo boxes
        allBooksList = FXCollections.observableArrayList(new LivreController().getAllBooks());
        allReadersList = FXCollections.observableArrayList(new LecteurController().getAllReaders());
        borrowedBooksList = FXCollections.observableArrayList(new EmpruntController().loadEmpruntsData());
        returningBooksList = FXCollections.observableArrayList(); // Initialize ReturningBooksList

        // Filter non-returned books and add them to ReturningBooksList
        returningBooksList.addAll(borrowedBooksList.stream()
                .filter(detailEmprunt -> detailEmprunt.getDateRetour() == null)
                .collect(Collectors.toList()));

        booksComboBox.setItems(allBooksList.stream().map(Livre::getTitre).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        readersComboBox.setItems(allReadersList.stream().map(lecteur -> lecteur.getNom() + " " + lecteur.getPrenom()).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        borrowedBooksComboBox.setItems(returningBooksList.stream().map(detailEmprunt -> detailEmprunt.getBookname() + " - " + detailEmprunt.getLecteurName()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList())));
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // Handle exit action
    }

    @FXML
    private void borrowBook(ActionEvent event) {
        String selectedBookTitle = booksComboBox.getValue();
        String selectedReaderName = readersComboBox.getValue();

        if (selectedBookTitle != null && selectedReaderName != null) {
            Livre selectedBook = allBooksList.stream().filter(livre -> livre.getTitre().equals(selectedBookTitle)).findFirst().orElse(null);
            Lecteur selectedReader = allReadersList.stream().filter(lecteur -> (lecteur.getNom() + " " + lecteur.getPrenom()).equals(selectedReaderName)).findFirst().orElse(null);

            if (selectedBook != null && selectedReader != null) {
                EmpruntController empruntController = new EmpruntController();

                // Check if the book is already borrowed by the reader
                if (empruntController.isLivreEmprunteByUser(selectedBook, selectedReader)) {
                    AlertMaker.showSimpleAlert("Emprunté", selectedBook.getTitre() + " est déjà emprunté par " + selectedReader.getNom() + " " + selectedReader.getPrenom());
                    return; // Stop further processing
                }

                empruntController.emprunterLivre(selectedReader, selectedBook);

                // Refresh borrowed books list
                AlertMaker.showSimpleAlert("Emprunte", selectedBook.getTitre() + " empruntée avec succès par " + selectedReader.getNom() + " " + selectedReader.getPrenom());
                borrowedBooksList.clear();
                returningBooksList.clear();
                borrowedBooksList.addAll(empruntController.loadEmpruntsData());
                returningBooksList.addAll(borrowedBooksList.stream()
                        .filter(detailEmprunt -> detailEmprunt.getDateRetour() == null)
                        .collect(Collectors.toList()));
                borrowedBooksComboBox.setItems(returningBooksList.stream().map(detailEmprunt -> detailEmprunt.getBookname() + " - " + detailEmprunt.getLecteurName()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList())));
            } else {
                System.out.println("Invalid selection.");
            }
        } else {
            System.out.println("Please select a book and a reader.");
        }
    }

    @FXML
    private void returnBook(ActionEvent event) {
        String selectedBookTitle = borrowedBooksComboBox.getValue();

        if (selectedBookTitle != null) {
            // Split the selected value to extract book title and reader name
            String[] parts = selectedBookTitle.split(" - ");
            String selectedBookName = parts[0];
            String selectedReaderName = parts[1];

            // Find the corresponding book and reader
            Livre selectedBook = allBooksList.stream()
                    .filter(livre -> livre.getTitre().equals(selectedBookName))
                    .findFirst()
                    .orElse(null);

            Lecteur selectedReader = allReadersList.stream()
                    .filter(lecteur -> (lecteur.getNom() + " " + lecteur.getPrenom()).equals(selectedReaderName))
                    .findFirst()
                    .orElse(null);

            if (selectedBook != null && selectedReader != null) {
                EmpruntController empruntController = new EmpruntController();

                // Check if the book is borrowed by the reader and has not been returned
                if (empruntController.isLivreEmprunteByUser(selectedBook, selectedReader) && empruntController.isLivreNotReturned(selectedBook, selectedReader)) {
                    // Return the book
                    empruntController.retournerLivre(selectedReader, selectedBook);

                    // Refresh borrowed books list (excluding returned books)
                    borrowedBooksList.clear();
                    returningBooksList.clear();
                    borrowedBooksList.addAll(empruntController.loadEmpruntsData());
                    returningBooksList.addAll(borrowedBooksList.stream()
                            .filter(detailEmprunt -> detailEmprunt.getDateRetour() == null)
                            .collect(Collectors.toList()));
                    borrowedBooksComboBox.setItems(returningBooksList.stream().map(detailEmprunt -> detailEmprunt.getBookname() + " - " + detailEmprunt.getLecteurName()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList())));


                    AlertMaker.showSimpleAlert("Retour", selectedBook.getTitre() + " retourné avec succès par " + selectedReader.getNom() + " " + selectedReader.getPrenom());
                } else {
                    AlertMaker.showSimpleAlert("Non Emprunté", selectedBook.getTitre() + " n'est pas emprunté ou déjà retourné par " + selectedReader.getNom() + " " + selectedReader.getPrenom());
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } else {
            System.out.println("Please select a borrowed book.");
        }
    }


    @FXML
    private void handleMenuViewBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("liste-livres-view.fxml"), "Listes des Livres", null);
    }


    @FXML
    private void handleMenuViewLecteur(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("liste-lecteurs-view.fxml"), "Liste Des Lecteurs", null);
    }

    @FXML
    private void handleIssuedList(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("liste-emprunt-view.fxml"), "Issued Book List", null);

    }


}
