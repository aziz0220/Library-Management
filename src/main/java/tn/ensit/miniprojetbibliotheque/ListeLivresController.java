package tn.ensit.miniprojetbibliotheque;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import tn.ensit.miniprojetbibliotheque.database.DatabaseHandler;
import tn.ensit.miniprojetbibliotheque.database.LivreController;
import tn.ensit.miniprojetbibliotheque.models.Livre;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;

public class ListeLivresController implements Initializable {

    ObservableList<Livre> list = FXCollections.observableArrayList();

    private String selectedSearchType;

    @FXML
    private TextField searchField;

    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<Livre> tableView;
    @FXML
    private TableColumn<Livre, String> titreCol;
    @FXML
    private TableColumn<Livre, String> isbnCol;
    @FXML
    private TableColumn<Livre, String> auteurCol;

    @FXML
    private AnchorPane contentPane;
    @FXML
    private ToggleButton toggleButtonTitle;
    @FXML
    private ToggleButton toggleButtonAuthor;
    @FXML
    private ToggleButton toggleButtonFirstLetters;
    @FXML
    private ToggleGroup toggleButtonGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleButtonGroup = new ToggleGroup();
        toggleButtonTitle.setToggleGroup(toggleButtonGroup);
        toggleButtonAuthor.setToggleGroup(toggleButtonGroup);
        toggleButtonFirstLetters.setToggleGroup(toggleButtonGroup);
        initCol();
        loadData();
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    private void initCol() {
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurCol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
    }

    private void loadData() {
        list.clear();
        LivreController livreController = new LivreController();
        List<Livre> books = livreController.getAllBooks();
        list.addAll(books);
        tableView.setItems(list);
    }

    @FXML
    private void handleBookDeleteOption(ActionEvent event) {
        Livre selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("No book selected", "Please select a book for deletion.");
            return;
        }
        if (DatabaseHandler.getInstance().isBookAlreadyIssued(selectedForDeletion)) {
            AlertMaker.showErrorMessage("Cant be deleted", "This book is already issued and cant be deleted.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression du livre");
        alert.setContentText("Are you sure want to delete the book " + selectedForDeletion.getTitre() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            LivreController livreController = new LivreController();
            Boolean result = livreController.supprimerLivre(selectedForDeletion.getCode());
            if (result) {
                AlertMaker.showSimpleAlert("Book deleted", selectedForDeletion.getTitre() + " was deleted successfully.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Failed", selectedForDeletion.getTitre() + " could not be deleted");
            }
        } else {
            AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
        }
    }


    @FXML
    private void handleBookEditOption(ActionEvent event) {
        //Fetch the selected row
        Livre selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("No book selected", "Please select a book for edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajout-livre-view.fxml"));
            Parent parent = loader.load();

            AjoutLivreController controller = (AjoutLivreController) loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book");
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
    private void exportAsPDF(ActionEvent event) {
        List<List> printData = new ArrayList<>();
        String[] headers = {"   Title   ", "Auteur", "  ISBN  "};
        printData.add(Arrays.asList(headers));
        for (Livre book : list) {
            List<String> row = new ArrayList<>();
            row.add(book.getTitre());
            row.add(book.getAuteur());
            row.add(String.valueOf(book.getISBN()));
            printData.add(row);
        }
        LibraryAssistantUtil.initPDFExport(rootPane, contentPane, getStage(), printData);
    }

    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }


    @FXML
    void searchLivres(ActionEvent event) {
        String searchType = handleToggleButtonAction();
        if (searchType != null) {
            LivreController livreController = new LivreController();
            switch (searchType) {
                case "titre":
                    // Search books by title
                    List<Livre> booksWithTitle = livreController.rechercher(searchField.getText()).stream()
                            .filter(book -> book.getTitre().contains(searchField.getText()))
                            .collect(Collectors.toList());
                    list.clear();
                    list.addAll(booksWithTitle);
                    tableView.setItems(list);
                    break;

                case "Auteur":
                    // Search books by author
                    List<Livre> booksWithAuthor = livreController.rechercher(searchField.getText()).stream()
                            .filter(book -> book.getAuteur().contains(searchField.getText()))
                            .collect(Collectors.toList());
                    list.clear();
                    list.addAll(booksWithAuthor);
                    tableView.setItems(list);
                    break;

                case "first":
                    // Search books by first few letters of the title
                    List<Livre> booksWithFirstLetters = livreController.rechercher(searchField.getText()).stream()
                            .filter(book -> book.getTitre().startsWith(searchField.getText()))
                            .collect(Collectors.toList());
                    list.clear();
                    list.addAll(booksWithFirstLetters);
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
    void showAddLivrePopup(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("ajout-livre-view.fxml"), "Add New Book", null);
    }


    @FXML
    public String handleToggleButtonAction() {
        Toggle selectedToggleButton = toggleButtonGroup.getSelectedToggle();

        if (selectedToggleButton != null) {
            if (selectedToggleButton == toggleButtonTitle) {
                selectedSearchType = "titre";
            } else if (selectedToggleButton == toggleButtonAuthor) {
                selectedSearchType = "Auteur";
            } else if (selectedToggleButton == toggleButtonFirstLetters) {
                selectedSearchType = "first";
            }
        }
        return selectedSearchType;
    }

}
