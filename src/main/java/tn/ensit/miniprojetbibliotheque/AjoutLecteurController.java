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
import tn.ensit.miniprojetbibliotheque.database.LecteurController;
import tn.ensit.miniprojetbibliotheque.models.Lecteur;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AjoutLecteurController implements Initializable {
    private long CIN;

    @FXML
    private TextField cin;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private LecteurController lecteurController;

    private Boolean isInEditMode = Boolean.FALSE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lecteurController = new LecteurController();
    }

    @FXML
    private void addReader(ActionEvent event) {
        String CINText = StringUtils.trimToEmpty(cin.getText());
        String nomText = StringUtils.trimToEmpty(nom.getText());
        String prenomText = StringUtils.trimToEmpty(prenom.getText());

        if (CINText.isEmpty() || nomText.isEmpty() || prenomText.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return;
        }

        if (isInEditMode) {
            handleEditOperation();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
            return;
        }

        long CIN = 0;
        try {
            CIN = Long.parseLong(CINText);
        } catch (NumberFormatException e) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Invalid CIN", "Please enter a valid CIN");
            return;
        }

        if (lecteurController.isLecteurExists(CIN)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Duplicate CIN", "Lecteur with same CIN exists.\nPlease use a new CIN");
            return;
        }

        Lecteur lecteur = new Lecteur(CIN, nomText, prenomText);
        lecteurController.ajouterLecteur(lecteur);
        AlertMaker.showSimpleAlert("New reader added", nomText + " " + prenomText + " has been added");
        clearEntries();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Lecteur lecteur) {
        this.CIN = lecteur.getCIN();
        cin.setText(String.valueOf(lecteur.getCIN()));
        nom.setText(lecteur.getNom());
        prenom.setText(lecteur.getPrenom());
        isInEditMode = Boolean.TRUE;
    }

    private void clearEntries() {
        cin.clear();
        nom.clear();
        prenom.clear();
    }

    private void handleEditOperation() {
        long newCIN = 0;
        try {
            newCIN = Long.parseLong(cin.getText());
        } catch (NumberFormatException e) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Invalid CIN", "Please enter a valid CIN");
            return;
        }

        if (lecteurController.editionLecteur(newCIN, nom.getText(), prenom.getText())) {
            AlertMaker.showSimpleAlert("Success", "Update complete");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed", "Could not update data");
        }
    }
}
