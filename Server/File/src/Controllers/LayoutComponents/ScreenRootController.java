package Controllers.LayoutComponents;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import Controllers.Controller;
import Controllers.Screens.ManualSortingScreenController;
import Controllers.Screens.SettingsScreenController;
import Models.Model;
import Models.Screens;
import Views.ThemeManager;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class ScreenRootController extends Controller {

	@FXML
	private Pane root;
	//private Screens screen;
	private HashMap<Screens, Pane> screen_panes;

	@Override
	public void init() {
		this.screen_panes = new HashMap<>();
		FXMLLoader loader;
		for (Screens screen : Screens.values()) {
			System.out.println("/Views/Screens/" + screen + "Screen.fxml");
			try {
				loader = new FXMLLoader(getClass().getResource("/Views/Screens/" + screen + "Screen.fxml"));
				this.screen_panes.put(screen, loader.load());
				Controller.model.getScreenControllers().put(screen, loader.getController());
				Controller.model.getScreenControllers().get(screen).init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.setScreen(Screens.Settings);
	}

	@Override
	public void setTheme() {
		for (Screens screen : Screens.values()) 
				Controller.model.getScreenControllers().get(screen).setTheme();
	}

	public void setScreen(Screens screen) {
		//this.screen = screen;
		this.root.getChildren().clear();
		try {
			System.out.println(screen);
			this.root.getChildren().add(this.screen_panes.get(screen));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public Screens getScreen() {
//		return this.screen;
//	}
}
