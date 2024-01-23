package tn.ensit.miniprojetbibliotheque;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil.ICON_IMAGE_LOC;

public class AccueilApplication extends Application {

    @Override
    public void start(Stage primaryStage){
        try {

            Parent root = FXMLLoader.load(getClass().getResource("accueil-view.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("biblio_styles.css").toExternalForm());
            primaryStage.getIcons().add(new Image(ICON_IMAGE_LOC));
            primaryStage.setTitle("Bibliotheque");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
