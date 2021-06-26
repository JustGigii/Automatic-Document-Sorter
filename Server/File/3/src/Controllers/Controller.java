package Controllers;

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

public abstract class Controller {
	protected double window_offset_x, window_offset_y;
	protected static Stage stage;
	protected static Model model = new Model();

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public abstract void init() throws Exception;
	public abstract void setTheme();
	
	public void initIcon(Pane icon, String folder, String name, int whidth, int hight, String color) {
		Rectangle clip = new Rectangle(whidth, hight, new ImagePattern(
				new Image(getClass().getResourceAsStream("/icons/" + folder +"/"+ name + ".png"), whidth, hight, false, false), 0, 0,
				whidth, hight, false));
		
		Rectangle fill = new Rectangle(whidth, hight);
		
		if(!icon.getChildren().isEmpty())
			icon.getChildren().clear();
		icon.getChildren().add(fill);
		icon.setClip(clip);
		icon.getChildren().get(0).setStyle(
				"-fx-fill:"+color+";");
		
	}
}
