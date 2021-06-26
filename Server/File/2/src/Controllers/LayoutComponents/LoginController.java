package Controllers.LayoutComponents;

import java.io.IOException;

import Controllers.Controller;
import Models.LayoutComponents;
import Views.ThemeManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginController extends Controller {
	
	@FXML
	private Pane layout;
	@FXML
	private ImageView background;
	@FXML
	private BorderPane background_pane;
	@FXML
	private Pane pane;
	
	@FXML
	private TextField username_field;
	@FXML
	private TextField password_field;
	@FXML
	private Pane button;
	
	@FXML
	private Text error_text;

	@Override
	public void init() {
		Controller.model.getLayoutComponentsControllers().put(LayoutComponents.Login, this);
		//Controller.model.setFromFolder("C:\\Users\\user\\Desktop\\SortFile\\File\\here");
		Timeline pause = new Timeline(new KeyFrame(Duration.millis(0.1), e -> {
			this.setTheme();
			Rectangle clip = new Rectangle(0, 0, this.layout.getWidth(), this.layout.getHeight());
			clip.setArcHeight(19);
			clip.setArcWidth(19);
			this.layout.setClip(clip);
		}));
		pause.play();
		//System.out.println("main loop");
		//Controller.model.mainLoop();
	}
	
	
	
	
	@FXML
	public void login(MouseEvent event)
	{
		System.out.println("login");
		if(this.username_field.getText().trim().isEmpty() || this.password_field.getText().trim().isEmpty()) {
			this.error_text.setText("Please Enter Your Credentials");
			this.error_text.setOpacity(1);
		}
		else {
			Controller.model.login(this.username_field.getText(), this.password_field.getText());
		}
	}
	
	public void setErorrText(String error_text) {
		this.error_text.setText(error_text);
		this.error_text.setOpacity(1);
	}
	
	public void reStart() {
		this.username_field.clear();
		this.password_field.clear();
		this.error_text.setOpacity(0);
		
		Controller.model.setAutomationStage(-1);
	}

	@Override
	public void setTheme() {
		this.background_pane.setStyle("-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");
		this.error_text.setFill(Color.RED);
		this.error_text.setEffect(new DropShadow(5,Color.RED));
//		this.pane.setStyle("-fx-background-radius: 10 10 10 10; -fx-border-radius: 10 10 10 10;"
//				+ "-fx-background-color:" + ThemeManager.backgroundSecondary() + ";");
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
	}
	

}
