package tn.ensit.miniprojetbibliotheque;

import javafx.scene.control.Alert;

public class AlertUtils {

    public static void showErrorAlert(String message) {
        showAlert(Alert.AlertType.ERROR, "Error", message);
    }

    public static void showInfoAlert(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Information", message);
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
