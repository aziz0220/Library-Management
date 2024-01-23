package tn.ensit.miniprojetbibliotheque;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import tn.ensit.miniprojetbibliotheque.alert.AlertMaker;
import tn.ensit.miniprojetbibliotheque.database.LivreController;
import tn.ensit.miniprojetbibliotheque.models.Livre;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AjoutLivreController implements Initializable {
    private long code;

    @FXML
    private TextField title;
    @FXML
    private TextField id;
    @FXML
    private TextField author;
    @FXML
    private TextField publisher;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private LivreController livreController;

    private Boolean isInEditMode = Boolean.FALSE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        livreController = new LivreController();

    }

    @FXML
    private void addBook(ActionEvent event) {
          String isbn = StringUtils.trimToEmpty(id.getText());
          String auteur = StringUtils.trimToEmpty(author.getText());
          long ISBN = 0;
          String titre = StringUtils.trimToEmpty(title.getText());
        if (isbn.isEmpty() || auteur.isEmpty() || titre.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return;
        }

        if (isInEditMode) {
            handleEditOperation();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
            return;
        }

        if (LivreController.isBookExists(isbn)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Duplicate book id", "Book with same Book ID exists.\nPlease use new ID");
            return;
        }

        Livre book = new Livre(titre, auteur, ISBN);
        Boolean result = livreController.ajouterLivre(book);
        if (result) {
           // AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "New book added", titre + " has been added");
            AlertMaker.showSimpleAlert("New book added", titre + " has been added");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to add new book", "Check all the entries and try again");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Livre book) {
        this.code = book.getCode();
        title.setText(book.getTitre());
        id.setText(String.valueOf(book.getISBN()));
        author.setText(book.getAuteur());
        isInEditMode = Boolean.TRUE;
    }

    private void clearEntries() {
        title.clear();
        id.clear();
        author.clear();
    }

    private void handleEditOperation() {
        if (livreController.editionLivre(this.code, title.getText(), author.getText(), id.hashCode())) {
            //AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success", "Update complete");
            AlertMaker.showSimpleAlert("Success", "Update complete");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed", "Could not update data");
        }
    }


}