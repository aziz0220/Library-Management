package tn.ensit.miniprojetbibliotheque.alert;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.ensit.miniprojetbibliotheque.util.LibraryAssistantUtil;

public class AlertMaker {

    public static void showSimpleAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showErrorMessage(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showErrorMessage(Exception ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error occured");
        alert.setHeaderText("Error Occured");
        alert.setContentText(ex.getLocalizedMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showErrorMessage(Exception ex, String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error occured");
        alert.setHeaderText(title);
        alert.setContentText(content);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<Button> controls, String header, String body) {
        BoxBlur blur = new BoxBlur(3, 3, 3);

        Dialog<Void> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getDialogPane().getStylesheets().add(AlertMaker.class.getResource("/tn/ensit/miniprojetbibliotheque/dark-theme.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-alert");


        VBox content = new VBox();
        content.setSpacing(10);

        Label headerLabel = new Label(header);
        Label bodyLabel = new Label(body);

        content.getChildren().addAll(headerLabel, bodyLabel);
        dialog.getDialogPane().setContent(content);


        List<ButtonType> buttonTypes = controls.stream()
                .map(button -> new ButtonType(button.getText(), ButtonBar.ButtonData.OK_DONE))
                .collect(Collectors.toList());

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypes);

        dialog.setOnCloseRequest(event -> nodeToBeBlurred.setEffect(null));

        Optional<Void> result = dialog.showAndWait();
        result.ifPresent(aVoid -> nodeToBeBlurred.setEffect(blur));


    }



    private static void styleAlert(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        LibraryAssistantUtil.setStageIcon(stage);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertMaker.class.getResource("/tn/ensit/miniprojetbibliotheque/dark-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
    }
}
