package tn.ensit.miniprojetbibliotheque;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;

public class SidebarController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    @FXML
    private void loadAddMember(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("ajout-lecteur-view.fxml"), "Add New Member", null);
    }
    @FXML
    private void loadAddBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("ajout-livre-view.fxml"), "Add New Book", null);
    }
    @FXML
    private void loadMemberTable(ActionEvent event) {
      LibraryAssistantUtil.loadWindow(getClass().getResource("liste-lecteurs-view.fxml"), "Member List", null);
    }
    @FXML
    private void loadBookTable(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("liste-livres-view.fxml"), "Book List", null);
    }
    @FXML
    private void loadIssuedBookList(ActionEvent event) {
        Object controller = LibraryAssistantUtil.loadWindow(getClass().getResource("liste-emprunt-view.fxml"), "Issued Book List", null);

    }
}
