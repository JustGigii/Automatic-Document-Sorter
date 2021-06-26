package Controllers.Screens.ScreensComponents;

import java.util.HashMap;

import Controllers.Controller;
import Models.Screens;
import Views.ThemeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class NetworkingTabController extends Controller {

	@FXML
	private Pane interface_icon;
	@FXML
	private Pane service_icon;
	@FXML
	private Pane ai_icon;
	@FXML
	private Pane database_icon;
	
	@FXML
	private Pane reload_icon;
	@FXML
	private Pane reopen_server_icon;
	
	@FXML
	private ImageView user_image;
	
	@FXML
	private Text user_name_text;
	@FXML
	private Pane user_name_pane;
	@FXML
	private Pane logout_icon;

	@Override
	public void init() {
		this.initIcon(interface_icon, "settings", "interface_icon", 24, 24, ThemeManager.currentMainly());
		this.initIcon(service_icon, "settings", "service_icon", 24, 24, ThemeManager.currentMainly());
		this.initIcon(ai_icon, "settings", "ai_icon", 24, 24, ThemeManager.currentMainly());
		this.initIcon(database_icon, "settings", "database_icon", 24, 24, ThemeManager.currentMainly());
		this.initIcon(reload_icon, "settings", "reload", 24, 24, ThemeManager.currentMainly());
		this.initIcon(reopen_server_icon, "settings", "open_server", 24, 24, ThemeManager.currentMainly());
		this.initIcon(logout_icon, "settings", "logout", 24, 24, ThemeManager.currentMainly());
		
		this.user_name_text.setFill(Color.valueOf(ThemeManager.currentMainly()));
		this.user_name_pane.setClip(new Rectangle(443.0, 24.0));
	}
	
	@Override
	public void setTheme() {
	}
	
	@FXML
	public void logout(MouseEvent event) {
		Controller.model.logout();
	}

	public void setUserImage(String image_url) {
		try {
			
		} catch (Exception e) {
			System.out.println(";( "+image_url);
		}
		this.user_image.setImage(new Image(image_url, 51, 51, false, false));
	}

	public void setUserName(String user_name) {
		this.user_name_text.setText(user_name);
	}
}