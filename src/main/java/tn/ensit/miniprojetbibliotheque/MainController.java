package tn.ensit.miniprojetbibliotheque;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.ensit.miniprojetbibliotheque.database.DatabaseHandler;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable{

    private DatabaseHandler databaseHandler;
    private PieChart bookChart;
    private PieChart memberChart;
    @FXML
    public HBox book_info;
    @FXML
    private Button emprunt;
    @FXML
    private HBox member_info;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    public AnchorPane mainAnchorPane;

    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;

    @FXML
    private StackPane bookInfoContainer;
    @FXML
    private StackPane memberInfoContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Instantiate EmpruntLivreController
        EmpruntLivreController empruntController = new EmpruntLivreController();
        // Set the reference to MainController
        empruntController.setMainController(this);
       databaseHandler = DatabaseHandler.getInstance();
        initDrawer();
        initGraphs();
    }

    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
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
    void handleEmpruntView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("emprunt-view.fxml"));
        Parent root = loader.load();

        // Get the controller instance from the loader
        EmpruntLivreController empruntController = loader.getController();
        // Set the reference to MainController
        empruntController.setMainController(this);
        Scene scene = emprunt.getScene();
        emprunt.setDisable(true);
        root.translateYProperty().set(scene.getHeight());
        mainAnchorPane.getChildren().remove(book_info);
        mainAnchorPane.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event0 -> {
            emprunt.setDisable(false);
        });
        timeline.play();
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

    private void initDrawer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sidebar.fxml"));
            VBox toolbar = loader.load();
            drawer.setSidePane(toolbar);
            loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            drawer.toggle();
        });
        drawer.setOnDrawerOpening((event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.toFront();
        });
        drawer.setOnDrawerClosed((event) -> {
            drawer.toBack();
            task.setRate(task.getRate() * -1);
            task.play();
        });
    }


    private void clearIssueEntries() {
        enableDisableGraph(true);
    }

    private void initGraphs() {
        bookChart = new PieChart(databaseHandler.getBookGraphStatistics());
        //memberChart = new PieChart(databaseHandler.getMemberGraphStatistics());
        bookInfoContainer.getChildren().add(bookChart);
       // memberInfoContainer.getChildren().add(memberChart);

//        bookIssueTab.setOnSelectionChanged((Event event) -> {
//            clearIssueEntries();
//            if (bookIssueTab.isSelected()) {
//                refreshGraphs();
//            }
//        });
    }

    private void refreshGraphs() {
        bookChart.setData(databaseHandler.getBookGraphStatistics());
      //  memberChart.setData(databaseHandler.getMemberGraphStatistics());
    }

    private void enableDisableGraph(Boolean status) {
        if (status) {
            bookChart.setOpacity(1);
        //    memberChart.setOpacity(1);
        } else {
            bookChart.setOpacity(0);
        //    memberChart.setOpacity(0);
        }
    }
    }
