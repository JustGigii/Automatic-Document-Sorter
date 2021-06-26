package Controllers.LayoutComponents;

import Controllers.Controller;
import Controllers.LayoutComponents.ScreenRootController;
import Models.LayoutComponents;
import Models.Model;
import Models.Screens;
import Views.ThemeManager;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MenuController extends Controller {

	@FXML
	private Pane menu;

	@FXML
	private Pane settings_pane;
	@FXML
	private Pane manual_sorting_pane;
	@FXML
	private Pane notifications_pane;

	@FXML
	private Pane settings_icon;
	@FXML
	private Pane manual_sorting_icon;
	@FXML
	private Pane notifications_icon;
	@FXML
	private Pane notifications_glow;
	@FXML
	private Pane notifications_ball;

	@FXML
	private Text settings_text;
	@FXML
	private Text manual_sorting_text;
	@FXML
	private Text notifications_text;

	private ScreenRootController screen_root_controller;

	private Screens screen;

	private boolean isNotifications;

	public void init() {
		this.isNotifications = false;
		Controller.model.getLayoutComponentsControllers().put(LayoutComponents.Menu, this);
		this.initIcon(settings_icon, "menu", "settings", 24, 24, ThemeManager.neutralSecondary());
		this.initIcon(manual_sorting_icon, "menu", "manual_sorting", 24, 24, ThemeManager.neutralSecondary());
		this.initIcon(notifications_icon, "menu", "notifications_" + this.isNotifications, 24, 24,
				ThemeManager.neutralSecondary());
		this.notifications_glow.getChildren()
				.add(new ImageView(new Image(
						getClass().getResourceAsStream("/icons/" + "menu" + "/" + "notifications_ball" + ".png"), 24,
						24, false, false)));
		this.notifications_glow.setEffect(new DropShadow(5,Color.RED));

		this.initIcon(notifications_ball, "menu", "notifications_ball", 24, 24, "red");
		this.screen_root_controller = null;
		this.screen = Screens.Settings;
	}

	public void connectToRoot(ScreenRootController screen_root_controller) {
		this.screen_root_controller = screen_root_controller;
	}

	@Override
	public void setTheme() {
		if (this.isNotifications) {
			notifications_ball.setOpacity(1);
			notifications_glow.setOpacity(1);
		} else {
			notifications_ball.setOpacity(0);
			notifications_glow.setOpacity(0);
		}

		this.settings_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
				+ "-fx-background-color:transparent;");
		this.manual_sorting_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
				+ "-fx-background-color:transparent;");
		this.notifications_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
				+ "-fx-background-color:transparent;");

		this.settings_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
		this.manual_sorting_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
		this.notifications_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");

		this.settings_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
		this.manual_sorting_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
		// this.notifications_icon.getChildren().get(0).setStyle("-fx-fill:" +
		// ThemeManager.neutralSecondary() + ";");
		this.initIcon(notifications_icon, "menu", "notifications_" + this.isNotifications, 24, 24,
				ThemeManager.neutralSecondary());

		switch (this.screen) {
		case Settings:
			this.settings_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");
			this.settings_text.setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.settings_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			break;
		case ManualSorting:
			this.manual_sorting_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");
			this.manual_sorting_text.setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.manual_sorting_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			break;
		case Notifications:
			this.notifications_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");
			this.notifications_text.setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.notifications_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			break;
		default:
			break;
		}

	}

	@FXML
	public void enteredSettings(MouseEvent event) {
		if (this.screen != Screens.Settings) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.settings_text.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.settings_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralSecondary()),
							Color.valueOf(ThemeManager.neutralMainly())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.settings_text.fillProperty(),
											Color.valueOf(ThemeManager.neutralSecondary()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.settings_text.fillProperty(),
									Color.valueOf(ThemeManager.neutralMainly())))));
			animation.play();
		}
	}

	@FXML
	public void exitedSettings(MouseEvent event) {
		if (this.screen != Screens.Settings) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.settings_text.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.settings_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralMainly()),
							Color.valueOf(ThemeManager.neutralSecondary())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.settings_text.fillProperty(),
											Color.valueOf(ThemeManager.neutralMainly()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.settings_text.fillProperty(),
									Color.valueOf(ThemeManager.neutralSecondary())))));
			animation.play();
		}
	}

	@FXML
	public void openSettings(MouseEvent event) {
		if (this.screen != Screens.Settings) {
			this.screen = Screens.Settings;
			this.screen_root_controller.setScreen(Screens.Settings);
			this.settings_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");
			this.manual_sorting_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:transparent;");
			this.notifications_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:transparent;");

			this.settings_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.manual_sorting_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
			this.notifications_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");

			this.settings_text.setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.manual_sorting_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
			this.notifications_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");

		}
	}

	@FXML
	public void enteredManualSorting(MouseEvent event) {
		if (this.screen != Screens.ManualSorting) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.manual_sorting_icon.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.manual_sorting_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralSecondary()),
							Color.valueOf(ThemeManager.neutralMainly())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.manual_sorting_text.fillProperty(),
											Color.valueOf(ThemeManager.neutralSecondary()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.manual_sorting_text.fillProperty(),
									Color.valueOf(ThemeManager.neutralMainly())))));
			animation.play();
		}
	}

	@FXML
	public void exitedManualSorting(MouseEvent event) {
		if (this.screen != Screens.ManualSorting) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.manual_sorting_icon.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.manual_sorting_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralMainly()),
							Color.valueOf(ThemeManager.neutralSecondary())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.manual_sorting_text.fillProperty(),
											Color.valueOf(ThemeManager.neutralMainly()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.manual_sorting_text.fillProperty(),
									Color.valueOf(ThemeManager.neutralSecondary())))));
			animation.play();
		}
	}

	@FXML
	public void openManualSorting(MouseEvent event) {
		if (this.screen != Screens.ManualSorting) {
			this.screen = Screens.ManualSorting;
			this.screen_root_controller.setScreen(Screens.ManualSorting);
			this.settings_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:transparent;");
			this.manual_sorting_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");
			this.notifications_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:transparent;");

			this.settings_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
			this.manual_sorting_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.notifications_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");

			this.settings_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
			this.manual_sorting_text.setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			this.notifications_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
		}
	}

	@FXML
	public void enteredNotifications(MouseEvent event) {
		if (this.screen != Screens.Notifications) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.notifications_text.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.notifications_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralSecondary()),
							Color.valueOf(ThemeManager.neutralMainly())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.notifications_text.fillProperty(),
											Color.valueOf(ThemeManager.neutralSecondary()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.notifications_text.fillProperty(),
									Color.valueOf(ThemeManager.neutralMainly())))));
			animation.play();
		}
	}

	@FXML
	public void exitedNotifications(MouseEvent event) {
		if (this.screen != Screens.Notifications) {
			BoxBlur effect = new BoxBlur(0, 0, 1);
			this.notifications_icon.setEffect(effect);

			ParallelTransition animation = new ParallelTransition(
					new FillTransition(Duration.seconds(0.25), (Shape) this.notifications_icon.getChildren().get(0),
							Color.valueOf(ThemeManager.neutralMainly()),
							Color.valueOf(ThemeManager.neutralSecondary())),
					new Timeline(
							new KeyFrame(Duration.ZERO,
									new KeyValue(this.notifications_text.fillProperty(),
											Color.valueOf(ThemeManager.neutralMainly()))),
							new KeyFrame(Duration.seconds(0.25), new KeyValue(this.notifications_text.fillProperty(),
									Color.valueOf(ThemeManager.neutralSecondary())))));
			animation.play();
		}
	}

	@FXML
	public void openNotifications(MouseEvent event) {
		if (this.screen != Screens.Notifications) {
			this.screen = Screens.Notifications;
			this.screen_root_controller.setScreen(Screens.Notifications);
			this.settings_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:transparent;");
			this.manual_sorting_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:transparent;");
			this.notifications_pane.setStyle("-fx-background-radius: 10 0 0 10; -fx-border-radius: 10 0 0 10;"
					+ "-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");

			this.settings_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
			this.manual_sorting_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
			this.notifications_icon.getChildren().get(0).setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");

			this.settings_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
			this.manual_sorting_text.setStyle("-fx-fill:" + ThemeManager.neutralSecondary() + ";");
			this.notifications_text.setStyle("-fx-fill:" + ThemeManager.currentMainly() + ";");
			
			this.isNotifications = false;
			this.setTheme();
		}
	}

	public void setNotifications(boolean isNotifications) {
		this.isNotifications = isNotifications;
	}

}

//
//
// this.settings_icon.setStyle("-fx-background-color:" +
// ThemeManager.neutralMainly() + ";"
// + "-fx-effect: dropshadow( gaussian , " + ThemeManager.currentMainly() + " ,
// 10, 0.5, 0.0, 0.0);");
// this.settings_text.setStyle("-fx-fill:" + ThemeManager.neutralMainly() + ";"
// + "-fx-effect: dropshadow( gaussian , " + ThemeManager.currentMainly() + " ,
// 10, 0.5, 0.0, 0.0);");
// this.settings_glow.getChildren()
// .add(new ImageView(new Image(getClass().getResourceAsStream("/icons/" +
// "settings_selected" + ".png"),
// 24, 24, false, false)));
// this.settings_glow.setStyle("-fx-background-color:transparent;" +
// "-fx-effect: dropshadow( gaussian , "
// + ThemeManager.currentMainly() + " , 10, 0.5, 0.0, 0.0);");