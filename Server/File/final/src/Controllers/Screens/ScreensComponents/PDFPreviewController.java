package Controllers.Screens.ScreensComponents;

import com.spire.pdf.PdfDocument;

import Controllers.Controller;
import Models.Screens;
import Views.ThemeManager;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PDFPreviewController extends Controller {

	@FXML
	private Pane pdf_preview;
	@FXML
	private BorderPane layout;
	@FXML
	private Pane name_label;
	@FXML
	private Pane checkbox;
	@FXML
	private Pane checkbox_inside;

	private boolean is_selected;
	private boolean is_open;
	private Image full_image;
	private ImageView preview_image_view;
	private String folder;
	private String name;
	// ------------------------------------------------------------------------------------------------------
	private Tooltip name_tooltip;

	@Override
	public void init() {
		this.is_selected = false;
		this.is_open = false;
		this.initIcon(checkbox, "manual_sorting_screen", "checkbox", 18, 18, ThemeManager.neutralSecondary());
		this.setNameLabel();
	}

	public void init(String folder, String name) {
		this.name = name;
		this.folder = folder;
		this.showPDF();
		this.init();
	}

	public String getName() {
		return this.name;
	}

	public Image getFullImage() {
		return this.full_image;
	}

	public void showPDF() {
		System.out.println("cheackpoint1");

		PdfDocument pdf = new PdfDocument();
		pdf.loadFromFile(folder + "\\" + name + ".pdf");
		System.out.println(pdf);
		// System.out.println("cheackpoint2");
		// SimpleRenderer renderer = new SimpleRenderer();
		// renderer.setResolution(100);
		// renderer.setMaxProcessCount(1);
		// System.out.println("cheackpoint3");
		// java.util.List<java.awt.Image> pdf_list = null;
		// System.out.println(renderer);
		// System.out.println(document);
		// //renderer.setMaxProcessCount(100);
		// try {
		// pdf_list = renderer.render(document);
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (RendererException e) {
		// e.printStackTrace();
		// } catch (org.ghost4j.document.DocumentException e) {
		// e.printStackTrace();
		// }

		System.out.println("cheackpoint4 "+this.name);
		java.awt.image.BufferedImage img = pdf.saveAsImage(0);
		this.full_image = SwingFXUtils.toFXImage(img, null);

		Double scale = img.getWidth() >= img.getHeight() ? this.layout.getPrefWidth() / img.getWidth()
				: this.layout.getPrefHeight() / img.getHeight();
		int newW = (img.getWidth() * scale) >= this.layout.getPrefWidth() ? (int) this.layout.getPrefWidth()
				: (int) (img.getWidth() * scale);
		int newH = (img.getHeight() * scale) >= this.layout.getPrefHeight() ? (int) this.layout.getPrefHeight()
				: (int) (img.getHeight() * scale);

		java.awt.Image tmp = img.getScaledInstance(newW, newH, java.awt.Image.SCALE_SMOOTH);
		java.awt.image.BufferedImage dimg = new java.awt.image.BufferedImage(newW, newH,
				java.awt.image.BufferedImage.TYPE_INT_ARGB);

		java.awt.Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		System.out.println("cheackpoint5");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Image preview_image = SwingFXUtils.toFXImage(dimg, null);
				System.out.println(">>>" + preview_image.getWidth() + " " + preview_image.getHeight());

				preview_image_view = new ImageView(preview_image);
				preview_image_view.setFitWidth(newW);
				preview_image_view.setFitHeight(newH);

				preview_image_view.setTranslateX(0);
				preview_image_view.setTranslateY(0);
				// preview_image_view.setEffect(new
				// InnerShadow(70,Color.BLACK));
				layout.setCenter(preview_image_view);
			}
		});

		// } catch (RendererException | DocumentException e) {
		// e.printStackTrace();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void setTheme() {
		if (this.is_open) {
			this.name_label.setStyle("-fx-background-color: linear-gradient(to right, " + ThemeManager.neutralMainly()
					+ ", " + ThemeManager.neutralMainly() + ", " + ThemeManager.neutralMainly()
					+ ", rgba(0, 0, 0, 0.0))");
			preview_image_view.setEffect(null);
		} else {
			this.preview_image_view.setEffect(new InnerShadow(70, Color.BLACK));
			this.name_label.setStyle("-fx-background-color: linear-gradient(to right, "
					+ ThemeManager.neutralSecondary() + ", " + ThemeManager.neutralSecondary() + ", "
					+ ThemeManager.neutralSecondary() + ", rgba(0, 0, 0, 0.0))");
		}
		if (this.is_selected) {
			this.name_label.setStyle("-fx-background-color: linear-gradient(to right, " + ThemeManager.currentMainly()
					+ ", " + ThemeManager.currentMainly() + ", " + ThemeManager.currentMainly()
					+ ", rgba(0, 0, 0, 0.0))");
			this.checkbox.getChildren().get(0).setStyle("-fx-fill: " + ThemeManager.currentMainly() + ";");
			// this.checkbox.setOpacity(1);
			this.checkbox_inside.setOpacity(1);
		} else {
			this.checkbox.getChildren().get(0).setStyle("-fx-fill: " + ThemeManager.neutralSecondary() + ";");
			// this.checkbox.setOpacity(0.5);
			this.checkbox_inside.setOpacity(0.5);
		}
	}

	public void setNameLabel() {
		Text text = new Text(0, 0, this.name);
		text.setFont(Font.font("Arial Bold", 14.0));
		text.setTextOrigin(VPos.TOP);
		this.name_label.setClip(text);
		this.name_tooltip = new Tooltip(this.name);
		Tooltip.install(this.name_label, name_tooltip);
	}

	@FXML
	public void select(MouseEvent event) {
		this.is_selected = !this.is_selected;
		Controller.model.changeSelectedAmount(this.name, this.is_selected);
		Controller.model.getScreenControllers().get(Screens.ManualSorting).setTheme();
		this.setTheme();
	}

	public boolean isSelected() {
		return this.is_selected;
	}

	public boolean isOpen() {
		return this.is_open;
	}

	@FXML
	public void open(MouseEvent event) {
		if (event != null && this.is_open) {
			return;
		}
		this.is_open = true;
		Controller.model.openPreview(this.name);
		this.setTheme();
	}

	public void close() {
		this.is_open = false;
		this.setTheme();
	}

}