package tn.ensit.miniprojetbibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.ensit.miniprojetbibliotheque.callback.BookReturnCallback;
import tn.ensit.miniprojetbibliotheque.database.DatabaseHandler;
import tn.ensit.miniprojetbibliotheque.models.DetailEmprunt;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;
import tn.ensit.miniprojetbibliotheque.database.EmpruntController;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ListeEmpruntController implements Initializable {

    private ObservableList<DetailEmprunt> list = FXCollections.observableArrayList();
    private BookReturnCallback callback;

    @FXML
    private TableView<DetailEmprunt> tableView;
    @FXML
    private TableColumn<DetailEmprunt, Integer> idCol;
    @FXML
    private TableColumn<DetailEmprunt, String> bookIDCol;
    @FXML
    private TableColumn<DetailEmprunt, String> bookNameCol;
    @FXML
    private TableColumn<DetailEmprunt, Integer> lecteurCINCol;
    @FXML
    private TableColumn<DetailEmprunt, String> lecteurNameCol;
    @FXML
    private TableColumn<DetailEmprunt, Date> dateEmprunteCol;
    @FXML
    private TableColumn<DetailEmprunt, Integer> daysCol;
    @FXML
    private TableColumn<DetailEmprunt, Date> dateRetourCol;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookid"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        lecteurCINCol.setCellValueFactory(new PropertyValueFactory<>("lecteurCIN"));
        lecteurNameCol.setCellValueFactory(new PropertyValueFactory<>("lecteurName"));
        dateEmprunteCol.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        dateRetourCol.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));

        // Make sure to initialize the TableColumn objects before using them
        // e.g., lecteurNameCol = new TableColumn<>("Lecteur Name");
        // ...

        tableView.setItems(list);
    }


    public void setBookReturnCallback(BookReturnCallback callback) {
        this.callback = callback;
    }

    private void loadData() {
        list.clear();
        EmpruntController empruntController = new EmpruntController();
        List<DetailEmprunt> empruntsList = empruntController.loadEmpruntsData();
        list.addAll(empruntsList);
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    @FXML
    private void exportAsPDF(ActionEvent event) {
        List<List> printData = new ArrayList<>();
        String[] headers = { "      BOOK NAME       ", "    Lecteur NAME     ", "ISSUE DATE", "dateretour"};
        printData.add(Arrays.asList(headers));
        for (DetailEmprunt info : list) {
            List<String> row = new ArrayList<>();
            row.add(info.getBookname());
            row.add(info.getLecteurName());
            row.add(info.getDateEmprunt().toString());
            row.add(info.getDateRetour().toString());
            printData.add(row);
        }
        LibraryAssistantUtil.initPDFExport(rootPane, contentPane, getStage(), printData);
    }

    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    @FXML
    private void handleReturn(ActionEvent event) {
        DetailEmprunt detailEmprunt = tableView.getSelectionModel().getSelectedItem();
        if (detailEmprunt != null) {
            callback.loadBookReturn(String.valueOf(detailEmprunt.getBookid()));
        }
    }
}
