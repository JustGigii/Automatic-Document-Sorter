package Controllers.Screens.ScreensComponents;

import java.util.HashMap;

import Controllers.Controller;
import Models.Screens;
import Views.ThemeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

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
		
	}
	
	@Override
	public void setTheme() {
	}
}