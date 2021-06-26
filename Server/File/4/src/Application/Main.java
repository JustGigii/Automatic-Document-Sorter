package Application;

import Controllers.Controller;
import Controllers.LayoutComponents.LayoutController;
import Models.Model;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Main extends Application {
	private Parent root;
	private double window_offset_x, window_offset_y;
	private LayoutController controller;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LayoutComponents/Layout.fxml"));
			root = (Parent) loader.load();
			controller = (LayoutController) loader.getController();
			controller.setStage(primaryStage);
			controller.init();
			Scene scene = new Scene(root, 1066, 816);
			scene.setFill(Color.TRANSPARENT);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.TRANSPARENT);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
