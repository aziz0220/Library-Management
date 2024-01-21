package tn.ensit.miniprojetbibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.ensit.miniprojetbibliotheque.alert.AlertMaker;
import tn.ensit.miniprojetbibliotheque.database.LecteurController;
import tn.ensit.miniprojetbibliotheque.models.Lecteur;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ListeLecteursController implements Initializable {

    ObservableList<Lecteur> list = FXCollections.observableArrayList();

    private String selectedSearchType;

    @FXML
    private TextField searchField;

    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<Lecteur> tableView;
    @FXML
    private TableColumn<Lecteur, String> nomCol;
    @FXML
    private TableColumn<Lecteur, String> prenomCol;
    @FXML
    private TableColumn<Lecteur, String> cinCol;

    @FXML
    private AnchorPane contentPane;
    @FXML
    private ToggleButton toggleButtonNom;
    @FXML
    private ToggleButton toggleButtonPrenom;
    @FXML
    private ToggleButton toggleButtonCIN;
    @FXML
    private ToggleGroup toggleButtonGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleButtonGroup = new ToggleGroup();
        toggleButtonNom.setToggleGroup(toggleButtonGroup);
        toggleButtonPrenom.setToggleGroup(toggleButtonGroup);
        toggleButtonCIN.setToggleGroup(toggleButtonGroup);
        initCol();
        loadData();
    }

    private void initCol() {
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        cinCol.setCellValueFactory(new PropertyValueFactory<>("CIN"));
    }

    private void loadData() {
        list.clear();
        LecteurController lecteurController = new LecteurController();
        List<Lecteur> lecteurs = lecteurController.getAllReaders();
        list.addAll(lecteurs);
        tableView.setItems(list);
    }

    @FXML
    private void handleLecteurDeleteOption(ActionEvent event) {
        Lecteur selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("No lecteur selected", "Please select a lecteur for deletion.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression du lecteur");
        alert.setContentText("Are you sure want to delete the lecteur " + selectedForDeletion.getNom() + " " + selectedForDeletion.getPrenom() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            LecteurController lecteurController = new LecteurController();
            Boolean result = lecteurController.supprimerLecteur(selectedForDeletion.getCIN());
            if (result) {
                AlertMaker.showSimpleAlert("Lecteur deleted", selectedForDeletion.getNom() + " " + selectedForDeletion.getPrenom() + " was deleted successfully.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Failed", selectedForDeletion.getNom() + " " + selectedForDeletion.getPrenom() + " could not be deleted");
            }
        } else {
            AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
        }
    }

    @FXML
    private void handleLecteurEditOption(ActionEvent event) {
        // Fetch the selected row
        Lecteur selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("No lecteur selected", "Please select a lecteur for edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajout-lecteur-view.fxml"));
            Parent parent = loader.load();

            AjoutLecteurController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Lecteur");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryAssistantUtil.setStageIcon(stage);

            stage.setOnHiding((e) -> {
                handleRefresh(new ActionEvent());
            });

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    @FXML
    void searchLecteurs(ActionEvent event) {
        String searchType = handleToggleButtonAction();
        if (searchType != null) {
            LecteurController lecteurController = new LecteurController();
            switch (searchType) {
                case "nom":
                    // Search lecteurs by nom
                    List<Lecteur> lecteursWithNom = lecteurController.rechercher(searchField.getText()).stream()
                            .filter(lecteur -> lecteur.getNom().contains(searchField.getText()))
                            .collect(Collectors.toList());
                    list.clear();
                    list.addAll(lecteursWithNom);
                    tableView.setItems(list);
                    break;

                case "prenom":
                    // Search lecteurs by prenom
                    List<Lecteur> lecteursWithPrenom = lecteurController.rechercher(searchField.getText()).stream()
                            .filter(lecteur -> lecteur.getPrenom().contains(searchField.getText()))
                            .collect(Collectors.toList());
                    list.clear();
                    list.addAll(lecteursWithPrenom);
                    tableView.setItems(list);
                    break;

                case "cin":
                    // Search lecteurs by CIN

                    List<Lecteur> lecteursWithCIN = lecteurController.rechercher(searchField.getText()).stream()
                            .peek(lecteur -> System.out.println("CIN in List: " + lecteur.getCIN()))
                            .filter(lecteur -> String.valueOf(lecteur.getCIN()).trim().equals(searchField.getText()))
                            .collect(Collectors.toList());
                    System.out.println(lecteursWithCIN);
                    list.clear();
                    list.addAll(lecteursWithCIN);
                    tableView.setItems(list);
                    break;

                default:
                    break;
            }
        } else {
            AlertMaker.showSimpleAlert("Alert", "Please select a search type");
        }
    }


    @FXML
    void showAddLecteurPopup(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("ajout-lecteur-view.fxml"), "Add New Lecteur", null);
    }

    @FXML
    public String handleToggleButtonAction() {
        Toggle selectedToggleButton = toggleButtonGroup.getSelectedToggle();

        if (selectedToggleButton != null) {
            if (selectedToggleButton == toggleButtonNom) {
                selectedSearchType = "nom";
            } else if (selectedToggleButton == toggleButtonPrenom) {
                selectedSearchType = "prenom";
            } else if (selectedToggleButton == toggleButtonCIN) {
                selectedSearchType = "cin";
            }
        }
        return selectedSearchType;
    }
}
