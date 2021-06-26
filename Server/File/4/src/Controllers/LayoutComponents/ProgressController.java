package Controllers.LayoutComponents;

import Controllers.Controller;
import Models.LayoutComponents;
import Models.Model;
import Views.ThemeManager;
import javafx.animation.KeyFrame;
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

public class ProgressController extends Controller {

	@FXML
	private Pane login_and_info;

	@FXML
	private Pane p_icon;
	@FXML
	private Pane bar_1;
	@FXML
	private Pane bar_2;
	@FXML
	private Pane bar_3;
	@FXML
	private Pane bar_4;
	@FXML
	private Pane bar_5;

	private Pane[] bars;

	private int state;

	// @FXML
	// private Text username;
	// @FXML
	// private Text counter;

	public void init() {
		Controller.model.getLayoutComponentsControllers().put(LayoutComponents.Progress, this);
		this.bars = new Pane[5];
		this.bars[0] = this.bar_1;
		this.bars[1] = this.bar_2;
		this.bars[2] = this.bar_3;
		this.bars[3] = this.bar_4;
		this.bars[4] = this.bar_5;

		this.initIcon(p_icon, "temp", "p_icon", 32, 32, ThemeManager.neutralMainly());
		this.updateBar(0);
		this.setTheme();
		// this.username.setFill(Color.WHITE);
		// this.counter.setFill(Color.WHITE);
	}

	@Override
	public void setTheme() {
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  ");
		this.initIcon(p_icon, "temp", "p_icon", 32, 32, ThemeManager.neutralMainly());
		if (this.state>0) {
			for (Pane bar : this.bars)
				bar.setStyle("-fx-background-color:" + ThemeManager.neutralMainly() + ";");
			for (int i = 0; i < this.state; i++)
				this.bars[i].setStyle("-fx-background-color:" + ThemeManager.currentMainly() + ";");
		} else
			for (Pane bar : this.bars)
				bar.setStyle("-fx-background-color:" + ThemeManager.neutralSecondary() + ";");

	}

	public void updateBar(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}
}

// <Text fx:id="username" fill="red" layoutX="52.0" layoutY="46.0"
// strokeType="OUTSIDE" strokeWidth="0.0" text="Username"
// wrappingWidth="126.13671875">
// <font>
// <Font name="Arial Bold" size="18.0" />
// </font>
// </Text>

// <Text fx:id="counter" fill="red" layoutX="185.0" layoutY="65.0"
// strokeType="OUTSIDE" strokeWidth="0.0" text="1000/1000"
// wrappingWidth="70.13671875">
// <font>
// <Font name="Arial" size="14.0" />
// </font>
// </Text>