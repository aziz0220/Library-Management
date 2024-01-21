package tn.ensit.miniprojetbibliotheque;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class AccueilController {
    @FXML
    private Label welcomeText;


    @FXML
    private void handleExit(ActionEvent event) {
        // Handle the "Exit" menu item action
        System.exit(0);
    }



    @FXML
    private void openBooksInterface() {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("liste-livres-view.fxml"));
//            Scene scene = new Scene(root);
//            scene.getStylesheets().add(getClass().getResource("../../../dark-theme.css").toExternalForm());
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        welcomeText.setText("Manage Books : Open Manage Books Interface");
    }



    @FXML
    private void openReadersInterface() {
        // Add code to open the interface for managing readers
        welcomeText.setText("Manage Readers : Open Manage Readers Interface");
    }

    @FXML
    private void openBorrowReturnInterface() {
        // Add code to open the interface for borrowing/returning books
        welcomeText.setText("Borrow/Return Books : Open Borrow/Return Interface");
    }

}