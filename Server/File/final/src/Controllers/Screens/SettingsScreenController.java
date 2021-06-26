package Controllers.Screens;

import java.util.HashMap;

import Controllers.Controller;
import Controllers.Screens.ScreensComponents.NetworkingTabController;
import Controllers.Screens.ScreensComponents.PrimaryTabController;
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
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SettingsScreenController extends Controller {

	@FXML
	private Text screen_title;
	
	@FXML
	private Text primary_label;
	@FXML
	private Pane primary_icon;
	@FXML
	private Pane primary_root;
	
	@FXML
	private Text networking_label;
	@FXML
	private Pane networking_icon;
	@FXML
	private Pane networking_root;
	
	private HashMap<SettingsTabs, Pane> settings_tabs_panes;
	private HashMap<SettingsTabs,Boolean> settings_tabs_states;

	@Override
	public void init() {
		this.settings_tabs_panes = new HashMap<>();
		this.settings_tabs_states = new HashMap<>();
		this.initIcon(primary_icon, "settings", "primary_icon", 24, 24, ThemeManager.neutralSecondary());
		this.initIcon(networking_icon, "settings", "networking_icon", 24, 24, ThemeManager.neutralSecondary());
		FXMLLoader loader;
		for (SettingsTabs settings_tab : SettingsTabs.values()) {
			try {
				loader = new FXMLLoader(getClass().getResource("/Views/Screens/ScreensComponents/" + settings_tab + "Tab.fxml"));
				this.settings_tabs_states.put(settings_tab, false);
				this.settings_tabs_panes.put(settings_tab, loader.load());
				System.out.println("]]]]]]]]]]]]]]]");
				Controller.model.getSettingsTabsControllers().put(settings_tab, loader.getController());
				Controller.model.getSettingsTabsControllers().get(settings_tab).init();
				System.out.println(")))))))))))))))))))))))");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.setTheme();
	}

	@Override
	public void setTheme() {
		this.primary_label.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
		this.networking_label.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");

		this.primary_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
		this.networking_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
		
		if (this.settings_tabs_states.get(SettingsTabs.Primary)){
			this.primary_label.setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.primary_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
		}
		if (this.settings_tabs_states.get(SettingsTabs.Networking)){
			this.networking_label.setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.networking_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
		}
		
		for (SettingsTabs settings_tab : SettingsTabs.values()) 
				Controller.model.getSettingsTabsControllers().get(settings_tab).setTheme();
	}
	
	@FXML
	public void enteredPrimary(MouseEvent event) {
		if (!this.settings_tabs_states.get(SettingsTabs.Primary)) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.primary_label.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.primary_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralSecondary()),
							Color.valueOf(ThemeManager.neutralMainly())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.primary_label.fillProperty(),
											Color.valueOf(ThemeManager.neutralSecondary()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.primary_label.fillProperty(),
									Color.valueOf(ThemeManager.neutralMainly())))));
			animation.play();
		}
	}

	@FXML
	public void exitedPrimary(MouseEvent event) {
		if (!this.settings_tabs_states.get(SettingsTabs.Primary)) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.primary_label.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.primary_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralMainly()),
							Color.valueOf(ThemeManager.neutralSecondary())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.primary_label.fillProperty(),
											Color.valueOf(ThemeManager.neutralMainly()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.primary_label.fillProperty(),
									Color.valueOf(ThemeManager.neutralSecondary())))));
			animation.play();
		}
	}

	@FXML
	public void openPrimary(MouseEvent event) {
		this.primary_label.setStyle("-fx-fill:" + ThemeManager.neutralMainly() + ";");
		this.primary_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralMainly() + ";");
		if (!this.settings_tabs_states.get(SettingsTabs.Primary)) {
			this.settings_tabs_states.put(SettingsTabs.Primary, true);
			this.primary_root.getChildren().add(this.settings_tabs_panes.get(SettingsTabs.Primary));
		}
		else{
			this.settings_tabs_states.put(SettingsTabs.Primary, false);
			this.primary_root.getChildren().clear();;
		}
	}
	
	@FXML
	public void enteredNetworking(MouseEvent event) {
		if (!this.settings_tabs_states.get(SettingsTabs.Networking)) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.networking_label.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.networking_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralSecondary()),
							Color.valueOf(ThemeManager.neutralMainly())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.networking_label.fillProperty(),
											Color.valueOf(ThemeManager.neutralSecondary()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.networking_label.fillProperty(),
									Color.valueOf(ThemeManager.neutralMainly())))));
			animation.play();
		}
	}

	@FXML
	public void exitedNetworking(MouseEvent event) {
		if (!this.settings_tabs_states.get(SettingsTabs.Networking)) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.networking_label.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.networking_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralMainly()),
							Color.valueOf(ThemeManager.neutralSecondary())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.networking_label.fillProperty(),
											Color.valueOf(ThemeManager.neutralMainly()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.networking_label.fillProperty(),
									Color.valueOf(ThemeManager.neutralSecondary())))));
			animation.play();
		}
	}

	@FXML
	public void openNetworking(MouseEvent event) {
		this.networking_label.setStyle("-fx-fill:" + ThemeManager.neutralMainly() + ";");
		this.networking_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralMainly() + ";");
		if (!this.settings_tabs_states.get(SettingsTabs.Networking)) {
			this.settings_tabs_states.put(SettingsTabs.Networking, true);
			this.networking_root.getChildren().add(this.settings_tabs_panes.get(SettingsTabs.Networking));
		}
		else{
			this.settings_tabs_states.put(SettingsTabs.Networking, false);
			this.networking_root.getChildren().clear();;
		}
	}
	
	
}