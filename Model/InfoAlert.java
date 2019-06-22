package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InfoAlert {
	
	public static void infoAlert(String titleBar, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static void errorAlert(String titleBar, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(titleBar);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static Alert confirmationAlert(String titleBar, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(null);
		alert.setContentText(content);
		
		return alert;
	}
}
