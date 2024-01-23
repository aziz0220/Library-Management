package tn.ensit.miniprojetbibliotheque;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.util.Duration;
import tn.ensit.miniprojetbibliotheque.callback.MainCallback;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;

public class SidebarController implements MainCallback {
    private MainController mainController;

    @FXML
    public Button quitButton;



    public void initialize() {
        mainController = new MainController();
        mainController.setSidebarController(this);
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

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    @FXML
    private void loadAccueilView(ActionEvent event) throws IOException {
        mainController.closeSession(event, quitButton);
}
}
