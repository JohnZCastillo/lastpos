package popup;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


public class Popup {

   final private static Alert alert = new Alert(AlertType.NONE);
 
    public static void warning(String message) {
        alert.setAlertType(AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void message(String message) {
        alert.setAlertType(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void error(String message) {
        alert.setAlertType(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
     public static boolean ask(String message) {
        alert.setAlertType(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }
}
