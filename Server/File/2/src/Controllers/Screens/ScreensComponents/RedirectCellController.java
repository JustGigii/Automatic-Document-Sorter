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

public class RedirectCellController extends Controller {

	@FXML
	private Text owner_1_text;
	@FXML
	private Text owner_2_text;

	@FXML
	private Pane owner_1_pane;
	@FXML
	private Pane owner_2_pane;

	private String from_owner;
	private String to_owner;
	private boolean is_selected;

	// ------------------------------------------------------------------------------------------------------

	@Override
	public void setTheme() {
		String style_1 = "-fx-border-width: 3;";
		String style_2 = "-fx-border-width: 3;";
		if (is_selected) {
			style_1 += "-fx-border-color:" + ThemeManager.currentMainly() + ";";
			style_2 += "-fx-border-color:" + ThemeManager.currentMainly() + ";";
		} else {
			style_1 += "-fx-border-color:" + ThemeManager.neutralSecondary() + ";";
			style_2 += "-fx-border-color:" + ThemeManager.neutralSecondary() + ";";
		}
		if (this.from_owner.equals(this.to_owner)) {
			style_1 += "-fx-border-radius:10 10 10 10;";
			style_2 += "-fx-border-radius:0 10 10 0;";
			this.owner_2_pane.setOpacity(0);
			this.owner_1_text.setFill(Color.valueOf("white"));
			this.owner_2_text.setFill(Color.valueOf(ThemeManager.neutralSecondary()));
		} else {
			style_1 += "-fx-border-radius:10 0 0 10;";
			style_2 += "-fx-border-radius:0 10 10 0;";
			this.owner_2_pane.setOpacity(1);
			this.owner_1_text.setFill(Color.valueOf(ThemeManager.neutralSecondary()));
			this.owner_2_text.setFill(Color.valueOf("white"));
		}
		this.owner_1_pane.setStyle(style_1);
		this.owner_2_pane.setStyle(style_2);
	}

	public boolean isSelected() {
		return this.is_selected;
	}
	
	public String getFromOwner() {
		return this.from_owner;
	}
	
	public String getToOwner() {
		return this.to_owner;
	}

	@Override
	public void init() throws Exception {
		this.is_selected = false;
		Rectangle clip = new Rectangle(0, 0, 250, 24);
		owner_1_pane.setClip(clip);
		owner_2_pane.setClip(clip);
	}

	public void init(String from_owner, String to_owner) {
		System.out.println("from-" + from_owner);
		System.out.println("to-" + to_owner);
		try {
			this.init();
		} catch (Exception e) {
		}
		this.from_owner = from_owner;
		this.to_owner = to_owner;
		this.owner_1_text.setText(from_owner);
		this.owner_2_text.setText(to_owner);
	}

	@FXML
	public void select(MouseEvent event) {
		this.is_selected = !this.is_selected;
		this.setTheme();
	}
	


}