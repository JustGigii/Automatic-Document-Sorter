package Controllers.Screens;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import Controllers.Controller;
import Controllers.LayoutComponents.MenuController;
import Controllers.Screens.ScreensComponents.NotificationsCellController;
import Views.ThemeManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class NotificationsScreenController extends Controller {

	private HashMap<String, NotificationsCellController> list;
	@FXML
	private VBox vbox;
	public void reStart() {
		
	}
	
	public void init() throws Exception {
		FXMLLoader loader;
		loader = new FXMLLoader(getClass().getResource("/Views/Screens/ScreensComponents/NotificationsCell.fxml"));
		vbox.getChildren().add(loader.load());
		NotificationsCellController n = (NotificationsCellController) loader.getController();
		n.init("Completed Automatic Sorting");
		
		
	}
	@Override
	public void setTheme() {
		// TODO Auto-generated method stub
		
	}
	// ------------------------------------------------------------------------------------------------------
}