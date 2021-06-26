package Controllers.Screens;

import org.apache.commons.lang3.StringUtils;

import Controllers.Controller;
import Views.ThemeManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ManualSortingScreenController extends Controller {

	@FXML
	private Pane root;
	@FXML
	private Text screen_title;

	@FXML
	private Text static_label_0;
	@FXML
	private Text static_label_1;
	@FXML
	private Text static_label_2;
	@FXML
	private Text static_label_3;
	@FXML
	private Text static_label_4;

	@FXML
	private Pane static_icon_1_0;
	@FXML
	private Pane static_icon_1_1_a;
	@FXML
	private Pane static_icon_1_1_b;
	@FXML
	private Pane static_icon_2_0;
	@FXML
	private Pane static_icon_2_1_a;
	@FXML
	private Pane static_icon_2_1_b;
	@FXML
	private Pane static_icon_3_0;
	@FXML
	private Pane static_icon_3_1_a;
	@FXML
	private Pane static_icon_3_1_b;
	@FXML
	private Pane static_icon_4_0;
	@FXML
	private Pane static_icon_4_1_a;
	@FXML
	private Pane static_icon_4_1_b;

	@FXML
	private Pane url_pane;
	@FXML
	private Pane reload_button;
	@FXML
	private Pane redirect_button;
	@FXML
	private Pane switch_button;
	@FXML
	private Text error_message;

	@FXML
	private Pane delete_button;
	@FXML
	private Pane delete_button_icon;
	@FXML
	private Text delete_button_text;
	@FXML
	private Pane move_button;
	@FXML
	private Pane move_button_icon;
	@FXML
	private Text move_button_text;

	@FXML
	private ScrollPane scroll_pane;
	@FXML
	private GridPane grid;

	@FXML
	private Pane pdf_pane;

	private ImageView pdf_view;
	private double[] pdf_view_sizes;
	private double pdf_offset_x;
	private double pdf_offset_y;
	private Tooltip current_folder_tooltip;
	private Tooltip redirect_button_tooltip;
	private Tooltip reload_button_tooltip;
	private Tooltip switch_button_tooltip;
	private boolean use_manual_sorting_folder;
	private String current_folder;
	// ------------------------------------------------------------------------------------------------------

	@Override
	public void init() {
		// Controller.model.getScreenControllers().put(Screens.ManualSorting,
		// this);

		this.pdf_view_sizes = new double[2];
		this.pdf_offset_x = 0;
		this.pdf_offset_y = 0;

		this.initIcon(redirect_button, "manual_sorting_screen", "redirect", 12, 12, ThemeManager.currentMainly());
		this.redirect_button_tooltip = new Tooltip("redirect in settings");
		Tooltip.install(this.redirect_button, this.redirect_button_tooltip);
		this.initIcon(reload_button, "manual_sorting_screen", "reload", 12, 12, ThemeManager.currentMainly());
		this.reload_button_tooltip = new Tooltip("reload folder");
		Tooltip.install(this.reload_button, this.reload_button_tooltip);
		this.initIcon(switch_button, "manual_sorting_screen", "switch", 12, 12, ThemeManager.currentMainly());
		this.redirect_button_tooltip = new Tooltip("switch folder");
		Tooltip.install(this.switch_button, this.redirect_button_tooltip);

		this.initIcon(static_icon_1_0, "manual_sorting_screen", "viewer_move", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_1_1_a, "manual_sorting_screen", "mouse", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_1_1_b, "manual_sorting_screen", "mouse_left", 12, 12, ThemeManager.neutralMainly());
		this.initIcon(static_icon_2_0, "manual_sorting_screen", "viewer_zoom", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_2_1_a, "manual_sorting_screen", "mouse", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_2_1_b, "manual_sorting_screen", "mouse_center", 12, 12, ThemeManager.neutralMainly());
		this.initIcon(static_icon_3_0, "manual_sorting_screen", "up_down", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_3_1_a, "manual_sorting_screen", "mouse", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_3_1_b, "manual_sorting_screen", "mouse_center", 12, 12, ThemeManager.neutralMainly());
		this.initIcon(static_icon_4_0, "manual_sorting_screen", "viewer_back", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_4_1_a, "manual_sorting_screen", "mouse", 12, 12, ThemeManager.neutralSecondary());
		this.initIcon(static_icon_4_1_b, "manual_sorting_screen", "mouse_right", 12, 12, ThemeManager.neutralMainly());

		this.initIcon(move_button_icon, "manual_sorting_screen", "move", 24, 24, ThemeManager.neutralMainly());
		this.initIcon(delete_button_icon, "manual_sorting_screen", "delete", 24, 24, ThemeManager.neutralMainly());

		this.pdf_pane.setClip(new Rectangle(this.pdf_pane.getPrefWidth(), this.pdf_pane.getPrefHeight()));

		this.use_manual_sorting_folder = true;
		this.reStartFolder(null);
		this.error_message.setOpacity(0);
		this.setTheme();
	}

	@Override
	public void setTheme() {
		this.url_pane.setStyle("-fx-background-color: linear-gradient(to right, " + ThemeManager.currentMainly() + ", "
				+ ThemeManager.currentMainly() + ", " + ThemeManager.currentMainly() + ", rgba(0, 0, 0, 0.0))");
		if (!Controller.model.getManualSortingSelectedFiles().isEmpty()) {
			this.move_button.setStyle("-fx-background-color:" + ThemeManager.currentMainly() + ";"
					+ " -fx-background-radius: 10 10 10 10; -fx-border-radius: 10 10 10 10;");
			this.delete_button.setStyle("-fx-background-color:" + ThemeManager.currentMainly() + ";"
					+ " -fx-background-radius: 10 10 10 10; -fx-border-radius: 10 10 10 10;");
		} else {
			this.move_button.setStyle("-fx-background-color:" + ThemeManager.neutralSecondary() + ";"
					+ " -fx-background-radius: 10 10 10 10; -fx-border-radius: 10 10 10 10;");
			this.delete_button.setStyle("-fx-background-color:" + ThemeManager.neutralSecondary() + ";"
					+ " -fx-background-radius: 10 10 10 10; -fx-border-radius: 10 10 10 10;");
		}
	}

	@FXML
	public void reStartFolder(MouseEvent event) {
		reStartScreen();
	}

	@FXML
	public void reStartScreen() {
		System.out.println(")))))))))))))))(***********************************8");
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				current_folder = Controller.model.getFromFolder();
				if (use_manual_sorting_folder)
					current_folder += "\\for_manual_sorting";
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Text text = new Text(0, 0,
								"'" + StringUtils.substringAfterLast(current_folder, "\\") + "' folder");
						text.setFont(Font.font("Arial Bold", 14.0));
						text.setTextOrigin(VPos.TOP);
						url_pane.setClip(text);
						current_folder_tooltip = new Tooltip(current_folder);
						Tooltip.install(url_pane, current_folder_tooltip);
					}
				});
				System.out.println("]]]]]]]]]]]]]]]]]]]]])))))))))))))))(***********************************8");
				Controller.model.extractFromFolder(current_folder);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						addPreviws();
						setTheme();
					}
				});
				return null;
			}
		};

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	public void addPreviws() {
		this.grid.getChildren().clear();
		if (Controller.model.getManualSortingPreviewPanes().isEmpty()) {
			this.error_message.setOpacity(1);
			this.error_message.setStyle("-fx-effect: dropshadow( gaussian , " + "red" + " , 10, 0.4, 0.0, 0.0);");
			this.pdf_pane.getChildren().clear();
		} else {
			this.error_message.setOpacity(0);
			int x = 0, y = 0;
			for (String f : Controller.model.getManualSortingFiles()) {
				if (x == 3) {
					x = 0;
					y++;
				}
				this.grid.add(Controller.model.getManualSortingPreviewPanes().get(f), x, y);
				if (Controller.model.getManualSortingPreviewControlers().get(f).isSelected())
					Controller.model.changeSelectedAmount(f, true);
				x++;
			}
			// this.showPDF(Controller.model.getPreviewControlers()
			// .get(Controller.model.getPreviewControlers().keySet().toArray()[0]).getFullImage());
		}
	}

	public void showPDF(Image full_image) {
		this.pdf_view = new ImageView(full_image);
		System.out.println(this.pdf_view.getFitWidth());

		Double scale = ((full_image.getWidth() >= full_image.getHeight())
				? (double) this.pdf_pane.getPrefWidth() / (double) full_image.getWidth()
				: (double) this.pdf_pane.getPrefHeight() / (double) full_image.getHeight());

		this.pdf_view.setFitWidth(full_image.getWidth() * scale);
		this.pdf_view_sizes[0] = (int) this.pdf_view.getFitWidth();
		this.pdf_view.setFitHeight(full_image.getHeight() * scale);
		this.pdf_view_sizes[1] = (int) this.pdf_view.getFitHeight();

		this.pdf_view.setTranslateX(0);
		this.pdf_view.setTranslateY(0);
		System.out.println(this.pdf_view.getFitWidth());

		this.pdf_pane.getChildren().clear();
		this.pdf_pane.getChildren().add(this.pdf_view);
		// this.pdf_pane.setCenter(this.pdf_view);
	}

	@FXML
	public void pdfPressed(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			pdf_offset_x = event.getX();
			pdf_offset_y = event.getY();
		}
	}

	@FXML
	public void pdfDragged(MouseEvent event) {
		if (this.pdf_view != null || !this.pdf_pane.getChildren().isEmpty()) {
			if (event.getButton() == MouseButton.PRIMARY) {
				this.pdf_view.setTranslateX(this.pdf_view.getTranslateX() + event.getX() - pdf_offset_x);
				this.pdf_view.setTranslateY(this.pdf_view.getTranslateY() + event.getY() - pdf_offset_y);

				System.out.println(this.pdf_view.getTranslateX() + " _ "
						+ (this.pdf_pane.getTranslateX() + this.pdf_pane.getWidth() - 40));

				if (this.pdf_view.getTranslateX() > this.pdf_pane.getTranslateX() + this.pdf_pane.getWidth() - 40)
					this.pdf_view.setTranslateX(this.pdf_pane.getTranslateX() + this.pdf_pane.getWidth() - 40);
				if (this.pdf_view.getTranslateX() + this.pdf_view.getFitWidth() < this.pdf_pane.getTranslateX() + 40)
					this.pdf_view.setTranslateX(this.pdf_pane.getTranslateX() + 40 - this.pdf_view.getFitWidth());

				if (this.pdf_view.getTranslateY() > this.pdf_pane.getTranslateY() + this.pdf_pane.getHeight() - 40)
					this.pdf_view.setTranslateY(this.pdf_pane.getTranslateY() + this.pdf_pane.getHeight() - 40);
				if (this.pdf_view.getTranslateY() + this.pdf_view.getFitHeight() < this.pdf_pane.getTranslateY() + 40)
					this.pdf_view.setTranslateY(this.pdf_pane.getTranslateY() + 40 - this.pdf_view.getFitHeight());

				pdf_offset_x = event.getX();
				pdf_offset_y = event.getY();
			}
		}
	}

	@FXML
	public void pdfZoomed(ScrollEvent event) {
		if (this.pdf_view != null || !this.pdf_pane.getChildren().isEmpty()) {
			try {
				this.pdf_view.setFitWidth(this.pdf_view.getFitWidth() + event.getDeltaY() * 2);
				this.pdf_view.setFitHeight(this.pdf_view.getFitHeight() + event.getDeltaY() * 2);

				if (this.pdf_view.getFitWidth() > 1.5 * this.pdf_view.getImage().getWidth()) {
					this.pdf_view.setFitWidth(1.5 * this.pdf_view.getImage().getWidth());
					this.pdf_view.setFitHeight(1.5 * this.pdf_view.getImage().getHeight());
				}
			} catch (Exception e) {
				double scale = ((this.pdf_view.getImage().getWidth() >= this.pdf_view.getImage().getHeight())
						? (double) this.pdf_pane.getPrefWidth() / (double) this.pdf_view.getImage().getWidth()
						: (double) this.pdf_pane.getPrefHeight() / (double) this.pdf_view.getImage().getHeight());
				this.pdf_view.setFitWidth(this.pdf_view.getImage().getWidth() * scale);
				this.pdf_view.setFitHeight(this.pdf_view.getImage().getHeight() * scale);
			}
			if ((int) this.pdf_view.getFitWidth() < this.pdf_view_sizes[0]
					|| (int) this.pdf_view.getFitHeight() < this.pdf_view_sizes[1]) {
				this.pdf_view.setFitWidth(this.pdf_view_sizes[0]);
				this.pdf_view.setFitHeight(this.pdf_view_sizes[1]);
			}
		}
	}

	@FXML
	public void pdfReset(MouseEvent event) {
		if (this.pdf_view != null || !this.pdf_pane.getChildren().isEmpty()) {
			if (event.getButton() == MouseButton.SECONDARY) {
				double scale = ((this.pdf_view.getImage().getWidth() >= this.pdf_view.getImage().getHeight())
						? (double) this.pdf_pane.getPrefWidth() / (double) this.pdf_view.getImage().getWidth()
						: (double) this.pdf_pane.getPrefHeight() / (double) this.pdf_view.getImage().getHeight());
				this.pdf_view.setFitWidth(this.pdf_view.getImage().getWidth() * scale);
				this.pdf_view.setFitHeight(this.pdf_view.getImage().getHeight() * scale);

				this.pdf_view.setTranslateX(0);
				this.pdf_view.setTranslateY(0);
			}
		}
	}

	@FXML
	public void switchCurrentFolder(MouseEvent event) {
		this.use_manual_sorting_folder = !this.use_manual_sorting_folder;
		reStartScreen();
	}

	@FXML
	public void moveFiles(MouseEvent event) {
		if (!Controller.model.getManualSortingSelectedFiles().isEmpty()) {
			Controller.model.moveSelectedFile(this.current_folder);
			this.reStartFolder(null);
		}
	}

	@FXML
	public void deleteFiles(MouseEvent event) {

	}

}