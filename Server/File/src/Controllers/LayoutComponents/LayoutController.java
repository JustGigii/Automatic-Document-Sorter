package Controllers.LayoutComponents;

import java.io.IOException;

import Controllers.Controller;
import Models.LayoutComponents;
import Views.ThemeManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LayoutController extends Controller {

	// private double window_offset_x = 0;
	// private double window_offset_y = 0;

	@FXML
	private Pane layout;
	@FXML
	private ImageView background;
	@FXML
	private Pane main_pane;

	@FXML
	private Pane background_pane;
	@FXML
	private Pane logo_pane;
	@FXML
	private BorderPane top_pane;
	@FXML
	private BorderPane left_pane;

	@FXML
	private Pane url_pane;

	@FXML
	private Text logo_text;

	private ScreenRootController screen_root_controller;
	private MenuController menu_controller;
	private WindowControlController window_control_controller;
	private ProgressController progress_controller;
	private Tooltip from_folder_tooltip;
	
	private Pane login_screen;
	private LoginController login_controller;

	@Override
	public void init() {
		Controller.model.getLayoutComponentsControllers().put(LayoutComponents.Layout, this);
		// Controller.model.setFromFolder("C:\\Users\\user\\Desktop\\SortFile\\File\\here");
		Timeline pause = new Timeline(new KeyFrame(Duration.millis(0.1), e -> {
			this.setTheme();
			Rectangle clip = new Rectangle(0, 0, this.layout.getWidth(), this.layout.getHeight());
			clip.setArcHeight(19);
			clip.setArcWidth(19);
			this.layout.setClip(clip);
			this.bluredGlass();

			FXMLLoader loader;
			try {
				loader = new FXMLLoader(getClass().getResource("/Views/LayoutComponents/ScreenRoot.fxml"));
				this.main_pane.getChildren().add(loader.load());
				this.screen_root_controller = (ScreenRootController) loader.getController();
				this.screen_root_controller.init();

				loader = new FXMLLoader(getClass().getResource("/Views/LayoutComponents/Menu.fxml"));
				this.left_pane.setTop(loader.load());
				this.menu_controller = (MenuController) loader.getController();
				this.menu_controller.init();
				this.menu_controller.connectToRoot(this.screen_root_controller);
				this.menu_controller.setTheme();

				loader = new FXMLLoader(getClass().getResource("/Views/LayoutComponents/WindowControl.fxml"));
				this.top_pane.setRight(loader.load());
				this.window_control_controller = (WindowControlController) loader.getController();
				this.window_control_controller.init();

				loader = new FXMLLoader(getClass().getResource("/Views/LayoutComponents/Progress.fxml"));
				this.left_pane.setBottom(loader.load());
				this.progress_controller = (ProgressController) loader.getController();
				this.progress_controller.init();

				loader = new FXMLLoader(getClass().getResource("/Views/LayoutComponents/Login.fxml"));
				this.login_screen=loader.load();
				login_controller = (LoginController) loader.getController();
				login_controller.init();
				login_controller.setTheme();

			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}));
		pause.play();
		// System.out.println("main loop");
		// Controller.model.mainLoop();
	}

	@Override
	public void setTheme() {
		this.background_pane.setStyle("-fx-background-color:" + ThemeManager.backgroundMainly() + ";");
		this.main_pane.setStyle("-fx-background-radius: 10 0 10 0; -fx-border-radius: 10 0 10 0;"
				+ "-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");
		this.top_pane.setStyle("-fx-background-color:" + ThemeManager.backgroundRightGradient() + ";");
		this.left_pane.setStyle("-fx-background-color:" + ThemeManager.backgroundBottomGradient() + ";");
		this.url_pane.setStyle("-fx-background-color: linear-gradient(to right, " + ThemeManager.currentMainly() + ", "
				+ ThemeManager.currentMainly() + ", " + ThemeManager.currentMainly() + ", rgba(0, 0, 0, 0.0))");
		this.logo_text.setStyle("-fx-background-color:" + ThemeManager.neutralMainly() + ";");

	}
	
	

	public void openLoginScreen() {
		if(!this.layout.getChildren().contains(login_screen)) {
			this.layout.getChildren().add(login_screen);
			login_controller.reStart();
		}
	}
	
	public void closeLoginScreen() {
		if(this.layout.getChildren().contains(login_screen)) {
			this.layout.getChildren().remove(login_screen);
			menu_controller.model.setAutomationStage(0);
		}
	}

	@FXML
	public void windowPressed(MouseEvent event) {
		window_offset_x = this.stage.getX() - event.getScreenX();
		window_offset_y = this.stage.getY() - event.getScreenY();
	}

	@FXML
	public void windowDragged(MouseEvent event) {
		this.stage.setX(event.getScreenX() + window_offset_x);
		this.stage.setY(event.getScreenY() + window_offset_y);
		this.background.setOpacity(0);
	}

	@FXML
	public void windowReleased(MouseEvent event) {
		if (this.background.getOpacity() == 0) {
			Timeline pause = new Timeline(new KeyFrame(Duration.millis(0.1), e -> {
				this.bluredGlass();
			}));
			pause.play();
		}
	}

	public void bluredGlass() {
		this.stage.hide();
		this.background.setImage(this.model.copyBackground((int) this.stage.getX(), (int) this.stage.getY(),
				(int) this.stage.getWidth(), (int) this.stage.getHeight()));
		this.background.setEffect(new BoxBlur(10, 10, 3));
		stage.show();
		this.background.setOpacity(1);
	}

	public void setURL(String url) {
		Text text = new Text(20.0, 15.0, url);
		text.setFont(Font.font("Arial", 14.0));
		text.setTextOrigin(VPos.TOP);
		this.url_pane.setClip(text);
		this.from_folder_tooltip = new Tooltip(url);
		Tooltip.install(url_pane, this.from_folder_tooltip);
		this.url_pane.setOpacity(1);
	}

	public void setNoURL() {
		this.url_pane.setOpacity(0);
	}

	/*
	 * @FXML private Pane layout;
	 * 
	 * @FXML private Pane menu_pane;
	 * 
	 * @FXML private VBox menu;
	 * 
	 * @FXML private ImageView menu_pane_background;
	 * 
	 * @FXML private Rectangle menu_pane_texture;
	 * 
	 * @FXML private Pane main_pane;
	 * 
	 * @FXML private ImageView main_pane_background;
	 * 
	 * @FXML private Rectangle main_pane_texture;
	 * 
	 * @FXML private Pane main_pane_glass;
	 * 
	 * @FXML private Button settings_button;
	 * 
	 * @FXML private Button view_and_edit_button;
	 * 
	 * @FXML private Button notifications_button;
	 * 
	 * @FXML private Pane bar;
	 * 
	 * @FXML private ImageView bar_background;
	 * 
	 * @FXML private Rectangle bar_texture;
	 * 
	 * @FXML private Pane bar_glass1;
	 * 
	 * @FXML private Pane bar_glass2;
	 * 
	 * private double window_offset_x, window_offset_y; private Stage stage; private
	 * Model model = new Model();
	 * 
	 * //---------------------------------------------------------------
	 * 
	 * public void setStage(Stage stage) { this.stage = stage; }
	 * 
	 * public void start() { Timeline pause = new Timeline(new
	 * KeyFrame(Duration.millis(0.1), e -> {
	 * 
	 * this.layout.setPrefWidth(this.stage.getWidth());
	 * this.layout.setPrefHeight(this.stage.getHeight());
	 * 
	 * this.bar.setLayoutX(0); this.bar.setLayoutY(0); this.bar.setPrefHeight(24);
	 * 
	 * this.model.setImageButton(this.settings_button,
	 * "/icons/settings_not_selected.png", 44, 34, 24, 24);
	 * this.model.setImageButton(this.view_and_edit_button,
	 * "/icons/view_and_edit_not_selected.png", 44, 34, 24, 24);
	 * this.model.setImageButton(this.notifications_button,
	 * "/icons/notifications_0_not_selected.png", 44, 34, 24, 24);
	 * 
	 * this.menu_pane.setLayoutX(0);
	 * this.menu_pane.setLayoutY(this.bar.getPrefHeight());
	 * this.menu.setPrefWidth(this.settings_button.getPrefWidth());
	 * this.menu_pane.setPrefWidth(this.settings_button.getPrefWidth());
	 * this.menu_pane.setPrefHeight(this.settings_button.getPrefHeight()*3);
	 * this.menu_pane_texture=new Rectangle(this.menu_pane.getPrefWidth(),
	 * this.menu_pane.getPrefHeight());
	 * 
	 * this.main_pane.setLayoutX(this.menu_pane.getPrefWidth());
	 * this.main_pane.setLayoutY(this.bar.getPrefHeight());
	 * 
	 * this.main_pane.setPrefHeight(this.stage.getHeight()-this.bar.
	 * getPrefHeight()); this.main_pane.setPrefWidth(this.stage.getWidth() -
	 * this.menu_pane.getWidth());
	 * this.main_pane_glass.setPrefHeight(this.main_pane.getPrefHeight());
	 * this.main_pane_glass.setPrefWidth(this.main_pane.getPrefWidth());
	 * this.main_pane_texture=new Rectangle(this.main_pane.getPrefWidth(),
	 * this.main_pane.getPrefHeight());
	 * 
	 * this.bar.setPrefWidth(this.menu_pane.getPrefWidth()+this.main_pane.
	 * getPrefWidth()); this.bar_glass1.setPrefHeight(this.bar.getPrefHeight());
	 * this.bar_glass2.setPrefHeight(this.bar.getPrefHeight());
	 * this.bar_glass1.setPrefWidth(200);
	 * this.bar_glass2.setPrefWidth(this.bar.getPrefWidth()-this.bar_glass1.
	 * getPrefWidth()); this.bar_glass1.setLayoutX(0);
	 * this.bar_glass2.setLayoutX(this.bar_glass1.getPrefWidth());
	 * this.bar_texture=new Rectangle(this.bar.getPrefWidth(),
	 * this.bar.getPrefHeight());
	 * 
	 * this.stage.hide();
	 * 
	 * this.model.GlassPaneFunction(this.stage, this.main_pane,
	 * this.main_pane_background, this.main_pane_texture);
	 * this.model.GlassPaneFunction(this.stage, this.menu_pane,
	 * this.menu_pane_background, this.menu_pane_texture);
	 * this.model.GlassPaneFunction(this.stage, this.bar, this.bar_background,
	 * this.bar_texture); stage.show(); })); pause.play(); }
	 * 
	 * @FXML public void screenPressed(MouseEvent event) { window_offset_x =
	 * this.stage.getX() - event.getScreenX(); window_offset_y = this.stage.getY() -
	 * event.getScreenY(); };
	 * 
	 * @FXML public void screenDragged(MouseEvent event) {
	 * this.stage.setX(event.getScreenX() + window_offset_x);
	 * this.stage.setY(event.getScreenY() + window_offset_y);
	 * this.main_pane_background.setOpacity(0);
	 * this.menu_pane_background.setOpacity(0); this.bar_background.setOpacity(0);
	 * };
	 * 
	 * @FXML public void screenReleased(MouseEvent event) {
	 * this.main_pane_background.setOpacity(1);
	 * this.menu_pane_background.setOpacity(1); this.bar_background.setOpacity(1);
	 * Timeline pause = new Timeline(new KeyFrame(Duration.millis(0.1), e -> {
	 * this.stage.hide(); this.model.GlassPaneFunction(this.stage, this.main_pane,
	 * this.main_pane_background, this.main_pane_texture);
	 * this.model.GlassPaneFunction(this.stage, this.menu_pane,
	 * this.menu_pane_background, this.menu_pane_texture);
	 * this.model.GlassPaneFunction(this.stage, this.bar, this.bar_background,
	 * this.bar_texture); stage.show(); })); pause.play(); }
	 * 
	 * @FXML public void openSettings(MouseEvent event) {
	 * this.model.setImageButton(this.settings_button,
	 * "/icons/settings_selected.png", 44, 34, 24, 24);
	 * this.model.setImageButton(this.view_and_edit_button,
	 * "/icons/view_and_edit_not_selected.png", 44, 34, 24, 24);
	 * this.model.setImageButton(this.notifications_button,
	 * "/icons/notifications_0_not_selected.png", 44, 34, 24, 24);
	 * 
	 * this.settings_button.
	 * setStyle("-fx-background-color: rgba(220, 20, 60, 0.85);" +
	 * "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.view_and_edit_button.
	 * setStyle("-fx-background-color: rgba(0, 14, 26,0.85);" +
	 * "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.notifications_button.
	 * setStyle("-fx-background-color: rgba(0, 14, 26,0.85);" +
	 * "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.bar_glass2.setStyle("-fx-background-color: rgba(220, 20, 60, 0.85);" );
	 * };
	 * 
	 * @FXML public void openViewAndEdit(MouseEvent event) {
	 * this.model.setImageButton(this.settings_button,
	 * "/icons/settings_not_selected.png", 44, 34, 24, 24);
	 * this.model.setImageButton(this.view_and_edit_button,
	 * "/icons/view_and_edit_selected.png", 44, 34, 24, 24);
	 * this.model.setImageButton(this.notifications_button,
	 * "/icons/notifications_0_not_selected.png", 44, 34, 24, 24);
	 * 
	 * this.settings_button. setStyle("-fx-background-color: rgba(0, 14, 26,0.85);"
	 * + "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.view_and_edit_button.
	 * setStyle("-fx-background-color: rgba(0, 139, 139,0.85);" +
	 * "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.notifications_button.
	 * setStyle("-fx-background-color: rgba(0, 14, 26,0.85);" +
	 * "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.bar_glass2.setStyle("-fx-background-color: rgba(0, 139, 139,0.85);" );
	 * };
	 * 
	 * @FXML public void openNotifications(MouseEvent event) {
	 * this.model.setImageButton(this.settings_button,
	 * "/icons/settings_not_selected.png", 44, 34, 24, 24);
	 * this.model.setImageButton(this.view_and_edit_button,
	 * "/icons/view_and_edit_not_selected.png", 44, 34, 24, 24);
	 * this.model.setImageButton(this.notifications_button,
	 * "/icons/notifications_0_selected.png", 44, 34, 24, 24);
	 * 
	 * this.settings_button. setStyle("-fx-background-color: rgba(0, 14, 26,0.85);"
	 * + "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.view_and_edit_button.
	 * setStyle("-fx-background-color: rgba(0, 14, 26,0.85);" +
	 * "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.notifications_button.
	 * setStyle("-fx-background-color: rgba(72, 61, 139,0.85);" +
	 * "-fx-background-radius: 0 0 0 0; -fx-border-radius: 0 0 0 0;");
	 * this.bar_glass2.setStyle("-fx-background-color: rgba(72, 61, 139,0.85);" );
	 * };
	 */

}
