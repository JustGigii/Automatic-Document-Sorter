package Controllers.Screens;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import Controllers.Controller;
import Controllers.LayoutComponents.MenuController;
import Controllers.Screens.ScreensComponents.NotificationsCellController;
import Models.LayoutComponents;
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

	// private HashMap<String, NotificationsCellController> cell_controller_list;
	// private HashMap<String, Pane> cell_pane_list;
	@FXML
	private VBox vbox;

	public void reStart() {
		this.vbox.getChildren().clear();
		// this.cell_controller_list.clear();
		// this.cell_pane_list.clear();
	}

	public void addNotifications(String type) {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/Views/Screens/ScreensComponents/NotificationsCell.fxml"));
			Pane notification = loader.load();
			vbox.getChildren().add(notification);
			//cell_pane_list.put(type, notification);
			NotificationsCellController notification_controller = (NotificationsCellController) loader.getController();
			notification_controller.init(type);
			//cell_controller_list.put(type, notification_controller);
			//cell_controller_list.get(type).init(type);
			;

			((MenuController) Controller.model.getLayoutComponentsControllers().get(LayoutComponents.Menu))
					.setNotifications(true);
			((MenuController) Controller.model.getLayoutComponentsControllers().get(LayoutComponents.Menu)).setTheme();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init() throws Exception {
	}

	@Override
	public void setTheme() {
		// TODO Auto-generated method stub

	}
	// ------------------------------------------------------------------------------------------------------
}