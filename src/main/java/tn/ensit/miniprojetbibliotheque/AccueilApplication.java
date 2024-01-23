package tn.ensit.miniprojetbibliotheque;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccueilApplication extends Application {

    @Override
    public void start(Stage primaryStage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("accueil-view.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("dark-theme.css").toExternalForm());
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
