package tn.ensit.miniprojetbibliotheque;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Initializable {
    @FXML
    private Button StartButton;
    @FXML
    public StackPane mainContainer;
    @FXML
    public AnchorPane startAnchor;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainController mainController = new MainController();

        mainController.setAccueilController(this);

    }

    @FXML
    private void handleStartButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        Scene scene = StartButton.getScene();
        StartButton.setDisable(true);
        root.translateYProperty().set(scene.getHeight());
        mainContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.3), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event0 -> {mainContainer.getChildren().remove(startAnchor);
            StartButton.setDisable(false);});
        timeline.play();
    }

    @FXML
    private void handleMenuClose(ActionEvent event) {
        getStage().close();
    }

    @FXML
    private void handleMenuAddBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("ajout-livre-view.fxml"), "Add New Book", null);
    }

    @FXML
    private void handleMenuAddMember(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("ajout-lecteur-view.fxml"), "Ajout d'un Lecteur", null);
    }

    @FXML
    private void handleMenuViewBook(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("liste-livres-view.fxml"), "Liste des Livres", null);
    }


    @FXML
    private void handleMenuViewMemberList(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("liste-lecteurs-view"), "Member List", null);
    }


    @FXML
    void handleEmpruntView(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("emprunt-view.fxml"), "Emprunter / Retourner Livre", null);
    }

    @FXML
    private void handleIssuedList(ActionEvent event) {
        LibraryAssistantUtil.loadWindow(getClass().getResource("liste-emprunt-view.fxml"), "Issued Book List", null);
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent event) {
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    private Stage getStage() {
        return (Stage) mainContainer.getScene().getWindow();
    }








}
