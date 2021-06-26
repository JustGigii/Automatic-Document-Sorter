package Controllers.Screens.ScreensComponents;

import java.util.HashMap;

import Controllers.Controller;
import Models.Screens;
import Models.SettingsTabs;
import Views.ThemeManager;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PrimaryTabController extends Controller {

	@FXML
	private Pane from_folder_button;
	@FXML
	private Pane to_folder_button;
	@FXML
	private Pane url_from_pane;
	@FXML
	private Pane url_to_pane;
	private Tooltip from_folder_tooltip;
	private Tooltip to_folder_tooltip;

	@FXML
	private VBox redirect_scroll_vbox;
	
	@FXML
	private Pane arrow;
	@FXML
	private Pane redirect_button;
	@FXML
	private TextField redirect_text_field;
	
	
	@FXML
	private Pane static_icon_1_1_a;
	@FXML
	private Pane static_icon_1_1_b;
	@FXML
	private Pane static_icon_1_0;
	@FXML
	private Text static_label_1;
	
	
	@Override
	public void init() {
		
		this.initIcon(static_icon_1_0, "manual_sorting_screen", "viewer_move", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_1_1_a, "manual_sorting_screen", "mouse", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_1_1_b, "manual_sorting_screen", "mouse_left", 12, 12, ThemeManager.neutralMainly());
		
		this.initIcon(from_folder_button, "settings", "from_folder", 24, 24, ThemeManager.currentMainly());
		this.initIcon(to_folder_button, "settings", "to_folder", 24, 24, ThemeManager.currentMainly());
		this.initIcon(redirect_button, "settings", "redirect", 24, 24, ThemeManager.currentMainly());
		this.initIcon(arrow, "settings", "arrow", 12, 12, ThemeManager.neutralSecondary());
		
		this.setURLFromPane("");
		this.setURLToPane("");
	}

	@Override
	public void setTheme() {
	}

	@FXML
	public void setFromFolder(MouseEvent event) {
		Controller.model.setFromFolder(Controller.model.directoryChooser());
	}

	@FXML
	public void setToFolder(MouseEvent event) {
		Controller.model.setToFolder(Controller.model.directoryChooser());
	}

	public void setURLFromPane(String url) {
		Text text = new Text(0, 8, url);
		text.setFont(Font.font("Arial", 14.0));
		text.setTextOrigin(VPos.TOP);
		this.url_from_pane.setClip(text);
		this.from_folder_tooltip = new Tooltip(url);
		Tooltip.install(url_from_pane, this.from_folder_tooltip);
		this.url_from_pane.setOpacity(1);
		if(url.contains("/")||url.contains("\\"))
			this.url_from_pane.setStyle("-fx-background-color: linear-gradient(to right, " + ThemeManager.currentMainly() + ", "
					+ ThemeManager.currentMainly() + ", " + ThemeManager.currentMainly() + ", rgba(0, 0, 0, 0.0))");
		else
			this.url_from_pane.setStyle("-fx-background-color: linear-gradient(to right, Red , Red , Red" + ", rgba(0, 0, 0, 0.0))");
	}

	public void setURLToPane(String url) {
		Text text = new Text(0, 8, url);
		text.setFont(Font.font("Arial", 14.0));
		text.setTextOrigin(VPos.TOP);
		this.url_to_pane.setClip(text);
		this.to_folder_tooltip = new Tooltip(url);
		Tooltip.install(url_to_pane, this.to_folder_tooltip);
		this.url_to_pane.setOpacity(1);
		if(url.contains("/")||url.contains("\\"))
			this.url_to_pane.setStyle("-fx-background-color: linear-gradient(to right, " + ThemeManager.currentMainly() + ", "
					+ ThemeManager.currentMainly() + ", " + ThemeManager.currentMainly() + ", rgba(0, 0, 0, 0.0))");
		else
			this.url_to_pane.setStyle("-fx-background-color: linear-gradient(to right, Red , Red , Red" + ", rgba(0, 0, 0, 0.0))");
	}
	
	public void clearRedirectCells() {
		this.redirect_scroll_vbox.getChildren().clear();
	}
	
	public void addRedirectCell(Pane redirect_cell_pane) {
		this.redirect_scroll_vbox.getChildren().add(redirect_cell_pane);
	}
	
	public void redirect() {
		Controller.model.redirect(this.redirect_text_field.getText());
	}

}