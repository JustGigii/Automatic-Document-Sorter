package Controllers.Screens.ScreensComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import Controllers.Controller;
import Models.Screens;
import Views.ThemeManager;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class NotificationsCellController extends Controller {

	@FXML
	private Pane cell;
	@FXML
	private Pane icon;
	@FXML
	private Text title;
	@FXML
	private Text text;
	
	private String type;

	// ------------------------------------------------------------------------------------------------------

	@Override
	public void setTheme() {
	}

	@Override
	public void init() throws Exception {
	}

	public void init(String type) {
		this.type=type;
		switch (type) {
		case "Completed Automatic Sorting":
			this.initIcon(icon, "notifications", "AI", 64, 64, ThemeManager.neutralMainly());
			this.title.setText("Automatic Sorting");
			this.text.setText("The files from "+Controller.model.getFromFolder()+" were successfully automatically sorted in "+Controller.model.getToFolder()+" by the AI.");
			this.cell.setClip(new Rectangle(754.0,100.0));
			break;
		case "Completed Manual Sorting":
			this.initIcon(icon, "notifications", "files", 64, 64, ThemeManager.neutralMainly());
			this.title.setText("Manual Sorting");
			this.text.setText("The files from "+Controller.model.getFromFolder()+" were successfully moved");
			this.cell.setClip(new Rectangle(754.0,100.0));
			break;
		case "AI Problem":
			this.initIcon(icon, "notifications", "AI", 64, 64, ThemeManager.neutralMainly());
			this.title.setText("Automatic Sorting");
			this.text.setText("The files from "+Controller.model.getFromFolder()+" some problem occurred with the AI, it may be because of slow connection to the internet.");
			this.cell.setClip(new Rectangle(754.0,100.0));
			break;
		case "new in manual":
			this.initIcon(icon, "notifications", "folder", 64, 64, ThemeManager.neutralMainly());
			this.title.setText("Manual Sorting");
			this.text.setText("New files are ready for manual sorting.");
			this.cell.setClip(new Rectangle(754.0,100.0));
			break;
		default:
			break;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}