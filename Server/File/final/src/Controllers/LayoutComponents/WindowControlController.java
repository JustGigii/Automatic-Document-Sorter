package Controllers.LayoutComponents;

import Controllers.Controller;
import Models.LayoutComponents;
import Models.Model;
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

public class WindowControlController extends Controller {

	@FXML
	private Pane x_icon;
	@FXML
	private Pane w_icon;
	@FXML
	private Pane m_icon;

	@FXML
	private Pane x_glow;

	@Override
	public void init() {
		Controller.model.getLayoutComponentsControllers().put(LayoutComponents.WindowControl, this);
		this.initIcon(x_icon, "window", "x", 24, 24, ThemeManager.currentMainly());
		this.initIcon(w_icon, "window", "w", 24, 24, ThemeManager.currentMainly());
		this.initIcon(m_icon, "window", "m", 24, 24, ThemeManager.currentMainly());
		
		w_icon.setOpacity(0);
		m_icon.setOpacity(0);

		this.x_glow.getChildren()
				.add(new ImageView(new Image(getClass().getResourceAsStream("/icons/" + "window" + "/" + "x" + ".png"),
						24, 24, false, false)));
		this.x_glow.setOpacity(0.0);

		this.setTheme();
	}

	@Override
	public void setTheme() {
		this.x_icon.setStyle("-fx-background-color:" + ThemeManager.currentMainly() + ";");
		this.w_icon.setStyle("-fx-background-color:" + ThemeManager.currentMainly() + ";");
		this.m_icon.setStyle("-fx-background-color:" + ThemeManager.currentMainly() + ";");

		this.x_glow.setStyle("-fx-background-color:transparent;" + "-fx-effect: dropshadow( gaussian , "
				+ ThemeManager.currentMainly() + " , 20, 0.7, 0.0, 0.0);");
	}

	@FXML
	public void enteredExit(MouseEvent event) {

		Timeline animation = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.x_glow.opacityProperty(), 0)),
				new KeyFrame(Duration.seconds(0.25), new KeyValue(this.x_glow.opacityProperty(), 1)));
		animation.play();

	}

	@FXML
	public void exitedExit(MouseEvent event) {

		Timeline animation = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.x_glow.opacityProperty(), 1)),
				new KeyFrame(Duration.seconds(0.25), new KeyValue(this.x_glow.opacityProperty(), 0)));
		animation.play();

	}

	@FXML
	public void exit(MouseEvent event) {
		Controller.model.exit();
	}

}
// Timeline pause = new Timeline(new KeyFrame(Duration.millis(0.1), e -> {
//
// this.stage.hide();
// this.background.setImage(this.model.copyBackground((int) this.stage.getX(),
// (int) this.stage.getY(),
// (int) this.stage.getWidth(), (int) this.stage.getHeight()));
//
// stage.show();
// }

//

//
// @FXML public void screenReleased(MouseEvent event) {
// this.main_pane_background.setOpacity(1);
// this.menu_pane_background.setOpacity(1);
// this.bar_background.setOpacity(1); Timeline pause = new Timeline(new
// KeyFrame(Duration.millis(0.1), e -> { this.stage.hide();
// this.model.GlassPaneFunction(this.stage, this.main_pane,
// this.main_pane_background, this.main_pane_texture);
// this.model.GlassPaneFunction(this.stage, this.menu_pane,
// this.menu_pane_background, this.menu_pane_texture);
// this.model.GlassPaneFunction(this.stage, this.bar, this.bar_background,
// this.bar_texture); stage.show(); })); pause.play(); }
