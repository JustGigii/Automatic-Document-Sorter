package Models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import Controllers.Controller;
import Controllers.LayoutComponents.LayoutController;
import Controllers.LayoutComponents.LoginController;
import Controllers.LayoutComponents.MenuController;
import Controllers.LayoutComponents.ProgressController;
import Controllers.Screens.ManualSortingScreenController;
import Controllers.Screens.ScreensComponents.PDFPreviewController;
import Controllers.Screens.ScreensComponents.PrimaryTabController;
import Controllers.Screens.ScreensComponents.RedirectCellController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

public class Model {
	// -primary
	private String from_folder;
	private String to_folder;// "C:\\Users\\user\\Desktop\\SortFile\\File\\here\\to";
	private HashMap<LayoutComponents, Controller> layout_components_controllers;
	private HashMap<Screens, Controller> screen_controllers;
	private boolean main_lock;

	// -for settings
	private HashMap<SettingsTabs, Controller> settings_tabs_controllers;

	// -for manual sorting
	private ArrayList<String> manual_sorting_files;
	private HashMap<String, Pane> manual_sorting_preview_panes;
	private HashMap<String, PDFPreviewController> manual_sorting_preview_controlers;
	private String manual_sorting_open_preview;
	private ArrayList<String> manual_sorting_selected_files;

	private HashMap<String, Pane> redirect_cell_panes;
	private HashMap<String, RedirectCellController> redirect_cell_controlers;

	// -for networking
	private ServiceConnector service_connector;
	// private boolean automation_lock_1;
	// private boolean automation_lock_2;
	private int automation_stage;

	// -------------------------------------------------------------------------------------------------------------------

	public Model() {
		this.manual_sorting_files = new ArrayList<>();
		this.layout_components_controllers = new HashMap<>();
		this.screen_controllers = new HashMap<>();
		this.manual_sorting_preview_panes = new HashMap<>();
		this.manual_sorting_preview_controlers = new HashMap<>();
		this.manual_sorting_open_preview = null;
		this.manual_sorting_selected_files = new ArrayList<>();
		this.settings_tabs_controllers = new HashMap<>();
		this.redirect_cell_panes = new HashMap<>();
		this.redirect_cell_controlers = new HashMap<>();

		this.main_lock = false;

		// this.service_connector = null;
		// this.automation_lock_1 = false;
		// this.automation_lock_2 = false;

		this.mainLoop();
	}
	
	public void setAutomationStage(int automation_stage) {
		this.automation_stage=automation_stage;
	}
	
	public int getAutomationStage() {
		return this.automation_stage;
	}

	public String getFromFolder() {
		return this.from_folder;
	}

	public String setFromFolder(String url) {
		System.out.println(url);
		if (url != null && isFolderExist(url)) {
			this.from_folder = url;
			this.createManualSortingFolder();
			((LayoutController) this.layout_components_controllers.get(LayoutComponents.Layout))
					.setURL(this.from_folder);

			((ManualSortingScreenController) this.screen_controllers.get(Screens.ManualSorting)).reStartScreen();
			((PrimaryTabController) this.settings_tabs_controllers.get(SettingsTabs.Primary))
					.setURLFromPane(this.from_folder);
			// service_connector.sendSetFromFolder(this.from_folder);
			service_connector.pushCommand(new HashMap<String, String>() {
				{
					put("type", "set_from_folder");
					put("path", from_folder);
				}
			});
			return this.from_folder;
		}
		((LayoutController) this.layout_components_controllers.get(LayoutComponents.Layout)).setNoURL();
		((ManualSortingScreenController) this.screen_controllers.get(Screens.ManualSorting)).reStartScreen();
		((PrimaryTabController) this.settings_tabs_controllers.get(SettingsTabs.Primary))
				.setURLFromPane("folder doesn't exist");
		return "folder doesn't exist";
	}

	public String setToFolder(String url) {
		System.out.println(url);
		if (url != null && isFolderExist(url)) {
			this.to_folder = url;
			((PrimaryTabController) this.settings_tabs_controllers.get(SettingsTabs.Primary))
					.setURLToPane(this.to_folder);
			// service_connector.sendSetToFolder(this.to_folder);
			service_connector.pushCommand(new HashMap<String, String>() {
				{
					put("type", "set_to_folder");
					put("path", to_folder);
				}
			});
			// service_connector.sendRedirectionListRequest(to_folder);

			return this.to_folder;
		}
		((PrimaryTabController) this.settings_tabs_controllers.get(SettingsTabs.Primary))
				.setURLToPane("folder doesn't exist");
		return "folder doesn't exist";
	}

	public ArrayList<String> getPDFs(String url) {
		File file = new File(url);

		String[] all_files = file.list();
		ArrayList<String> pdf_files = new ArrayList<String>();
		for (String f : all_files) {
			if (f.endsWith(".pdf")) {
				f = f.substring(0, f.length() - 4);
				pdf_files.add(f);
			}
		}
		return pdf_files;
	}

	public String directoryChooser() {
		DirectoryChooser directory_chooser = new DirectoryChooser();
		directory_chooser.setTitle("JavaFX Projects");
		try {
			directory_chooser.setInitialDirectory(new File(this.from_folder));
		} catch (Exception e) {
			directory_chooser.setInitialDirectory(null);
		}
		File path = directory_chooser.showDialog(null);
		System.out.println(path);
		return path.toString();
	}

	public void moveFile(String name, String from_folder, String to_folder) {
		Path temp = null;
		System.out.println("move file " + name);
		try {
			if (Files.exists(Paths.get(to_folder + "\\" + name + ".pdf")))
				Files.delete(Paths.get(to_folder + "\\" + name + ".pdf"));
			temp = Files.move(Paths.get(from_folder + "\\" + name + ".pdf"),
					Paths.get(to_folder + "\\" + name + ".pdf"));
			// service_connector.sendMoveFileRequest(name, from_folder, to_folder, "-", "-",
			// "manual sorting");
			service_connector.pushCommand(new HashMap<String, String>() {
				{
					put("type", "move_file");
					put("file_name", name);
					put("from_folder_path", from_folder);
					put("to_folder_path", to_folder);
					put("owner", "-");
					put("label", "-");
					put("way_of_sorting", "manual sorting");
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (temp != null) {
			System.out.println("File moved successfully");
		} else {
			System.out.println("Failed to move the file");
		}
	}

	public String createFolder(String new_to_folder) {
		Path dirPathObj = Paths.get(new_to_folder);
		boolean dirExists = Files.exists(dirPathObj);
		if (dirExists) {
			System.out.println(new_to_folder);
			System.out.println("Directory Already Exists");
			return "";
		} else {
			try {
				Files.createDirectories(dirPathObj);
				System.out.println("New directory successfully created");
				return "";
			} catch (IOException ioExceptionObj) {
				System.out.println("Problem occured while creating the directory" + ioExceptionObj.getMessage());
				return "Problem occured while creating the directory";
			}
		}
	}

	public Image copyBackground(int x, int y, int width, int height) {
		try {
			java.awt.Robot robot = new java.awt.Robot();
			java.awt.image.BufferedImage image = robot.createScreenCapture(new java.awt.Rectangle(x, y, width, height));
			return SwingFXUtils.toFXImage(image, null);
		} catch (java.awt.AWTException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void exit() {
		try {
			if (this.service_connector != null)
				this.service_connector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public boolean isFolderExist(String url) {
		return Files.exists(Paths.get(url));
	}

	public boolean isFolderEmpty(String url) {
		return this.getPDFs(url).isEmpty();
	}

	public boolean isMain_lock() {
		return main_lock;
	}

	// -for manual
	// sorting------------------------------------------------------------------------------------------------

	public String createManualSortingFolder() {
		return this.createFolder(this.from_folder + "\\for_manual_sorting");
	}

	public ArrayList<String> getManualSortingFiles() {
		return this.manual_sorting_files;
	}

	public HashMap<LayoutComponents, Controller> getLayoutComponentsControllers() {
		return this.layout_components_controllers;
	}

	public HashMap<Screens, Controller> getScreenControllers() {
		return this.screen_controllers;
	}

	public HashMap<String, Pane> getManualSortingPreviewPanes() {
		return this.manual_sorting_preview_panes;
	}

	public HashMap<String, PDFPreviewController> getManualSortingPreviewControlers() {
		return this.manual_sorting_preview_controlers;
	}

	public HashMap<SettingsTabs, Controller> getSettingsTabsControllers() {
		return this.settings_tabs_controllers;
	}

	public void extractFromFolder(String current_folder) {
		FXMLLoader loader;
		HashMap<String, Pane> new_preview_panes = new HashMap<>();
		HashMap<String, PDFPreviewController> new_preview_controlers = new HashMap<>();
		this.manual_sorting_selected_files.clear();
		this.manual_sorting_files.clear();
		this.manual_sorting_files = getPDFs(current_folder);
		System.out.println(this.manual_sorting_files);
		this.manual_sorting_preview_panes.clear();
		for (String f : this.manual_sorting_files) {
			System.out.println("_* " + f);
			if (this.manual_sorting_preview_panes.containsKey(f)) {
				System.out.println("existing preview");
				new_preview_panes.put(f, this.manual_sorting_preview_panes.get(f));
				new_preview_controlers.put(f, this.manual_sorting_preview_controlers.get(f));
			} else {
				System.out.println("new preview");
				try {
					loader = new FXMLLoader(getClass().getResource("/Views/Screens/ScreensComponents/PDFPreview.fxml"));
					System.out.println("extractFromFolder 1");
					new_preview_panes.put(f, loader.load());
					System.out.println("extractFromFolder 2");
					new_preview_controlers.put(f, loader.getController());
					System.out.println("extractFromFolder 3");
					new_preview_controlers.get(f).init(current_folder, f);
					System.out.println("extractFromFolder 4");
					new_preview_controlers.get(f).setTheme();
					System.out.println("extractFromFolder 5");
				} catch (Exception e) {
					System.out.println(e);
				}
				System.out.println("extractFromFolder 1");
			}
		}
		System.out.println("####################################");
		this.manual_sorting_preview_controlers = new_preview_controlers;
		this.manual_sorting_preview_panes = new_preview_panes;
		System.out.println("____________________ " + this.manual_sorting_open_preview);
		if (this.manual_sorting_files.isEmpty()) {
			this.manual_sorting_open_preview = null;
			return;
		}
		if (this.manual_sorting_open_preview == null
				|| !this.manual_sorting_files.contains(this.manual_sorting_open_preview)) {
			System.out.println("!!!!!!!!!!!!!!");
			new_preview_controlers.get(this.manual_sorting_files.get(0)).open(null);
			return;
		}
	}

	public void openPreview(String name) {
		System.out.print("1) " + this.manual_sorting_open_preview);
		if (this.manual_sorting_open_preview != null
				&& this.manual_sorting_preview_controlers.containsKey(this.manual_sorting_open_preview))
			this.manual_sorting_preview_controlers.get(this.manual_sorting_open_preview).close();
		this.manual_sorting_open_preview = name;
		((ManualSortingScreenController) this.screen_controllers.get(Screens.ManualSorting))
				.showPDF(this.manual_sorting_preview_controlers.get(this.manual_sorting_open_preview).getFullImage());
		System.out.print("2) " + this.manual_sorting_open_preview + "\n");
	}

	public ArrayList<String> getManualSortingSelectedFiles() {
		return this.manual_sorting_selected_files;
	}

	public void changeSelectedAmount(String name, boolean is_selected) {
		if (is_selected) {
			this.manual_sorting_selected_files.add(name);
			return;
		}
		this.manual_sorting_selected_files.remove(name);
	}

	public void moveSelectedFile(String current_folder) {
		String to_folder = this.directoryChooser();
		for (String f : this.manual_sorting_selected_files) {
			System.out.println("try to move " + f);
			try {
				this.moveFile(f, current_folder, to_folder);
			} catch (Exception e) {
				System.out.println("!!!!!!!!!!");
			}
		}
	}

	// -for automatic
	// sorting------------------------------------------------------------------------------------------------

	public String getManualSortingOpenPreview() {
		return manual_sorting_open_preview;
	}

	public void automaticlySortFiles(HashMap<String, HashMap<String, String>> automatic_sorting_dictionary) {
		for (String file : automatic_sorting_dictionary.keySet()) {
			Path temp = null;
			String new_to_folder = (automatic_sorting_dictionary.get(file).get("type").equals("NULL")
					|| automatic_sorting_dictionary.get(file).get("owner").equals("NULL"))
							? this.from_folder + "\\" + "for_manual_sorting"
							: this.to_folder + "\\" + automatic_sorting_dictionary.get(file).get("owner") + "\\"
									+ automatic_sorting_dictionary.get(file).get("type");

			try {
				createFolder(new_to_folder);
				System.out.println(new_to_folder + "\\" + file + ".pdf");
				if (Files.exists(Paths.get(new_to_folder + "\\" + file + ".pdf")))
					Files.delete(Paths.get(new_to_folder + "\\" + file + ".pdf"));
				temp = Files.move(Paths.get(automatic_sorting_dictionary.get(file).get("path")),
						Paths.get(new_to_folder + "\\" + file + ".pdf"));

				System.out.println(file + "============================================" + "1");
				if (!new_to_folder.contains("for_manual_sorting")) {
					// service_connector.sendMoveFileRequest(file, this.from_folder, this.to_folder,
					// automatic_sorting_dictionary.get(file).get("owner"),
					// automatic_sorting_dictionary.get(file).get("type"), "automatic AI sorting");
					//
					System.out.println(service_connector.getCommandStack().toString() + "  "
							+ service_connector.getAcceptsStack().toString());

					service_connector.pushCommand(new HashMap<String, String>() {
						{
							put("type", "move_file");
							put("file_name", file);
							put("from_folder_path", from_folder);
							put("to_folder_path", to_folder);
							put("owner", automatic_sorting_dictionary.get(file).get("owner"));
							put("label", automatic_sorting_dictionary.get(file).get("type"));
							put("way_of_sorting", "automatic AI sorting");
						}
					});

					System.out.println(file + "============================================" + "2");

					// String message="";
					// while(!message.equals("file "+file+" is updated in the data-base")){
					// message = service_connector.receive();
					// if (!message.equals("Error: faulty connection to service, please try agin"))
					// System.out.println("<message: " + message + ">");
					// }
					// }
					System.out.println(file + "============================================" + "3");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			if (temp != null) {
				System.out.println("File moved successfully");
			} else {
				System.out.println("Failed to move the file");
			}
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((ProgressController) layout_components_controllers.get(LayoutComponents.Progress)).updateBar(3);
				((ProgressController) layout_components_controllers.get(LayoutComponents.Progress)).setTheme();
			}
		});
		((ManualSortingScreenController) this.screen_controllers.get(Screens.ManualSorting)).reStartScreen();
	}

	public void mainLoop() {
		service_connector = new ServiceConnector();
		service_connector.setModel(this);
		automation_stage = 0;
		
		
		
//		((LayoutController) layout_components_controllers
//				.get(LayoutComponents.Layout)).openLoginScreen();
		
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				System.out.println("task started");
				System.out.println("+---------------------------{ automation stage " + automation_stage
						+ " }---------------------------+");
//				Platform.runLater(new Runnable() {
//					@Override
//					public void run() {
//
//						((ProgressController) layout_components_controllers
//								.get(LayoutComponents.Progress)).updateBar(automation_stage);
//						((ProgressController) layout_components_controllers
//								.get(LayoutComponents.Progress)).setTheme();
//					}
//				});
				AnimationTimer timer = new AnimationTimer() {
					private boolean lastMainLock = (from_folder != null && isFolderExist(from_folder)
							&& !isFolderEmpty(from_folder) && to_folder != null && isFolderExist(to_folder));
					private long lastUpdate = 0;
					private String message;

					@Override
					public void handle(long now) {
						if (now - lastUpdate >= 1) {
							message = service_connector.receive();
							if (!message.equals("Error: faulty connection to service, please try agin"))
								System.out.println("<message: " + message + ">");
							service_connector.sendCommand();
							
							
							
							if(service_connector.getUsername()==null) {
								((LayoutController)layout_components_controllers.get(LayoutComponents.Layout)).openLoginScreen();
							}
							
							
							
							
							switch (automation_stage) {
							case -1:
								break;
							case 0:
								service_connector.nullifyAi_started_analyzing_message();
								service_connector.nullifyFolder_empty_message();
								if (from_folder != null && isFolderExist(from_folder) && !isFolderEmpty(from_folder)
										&& to_folder != null && isFolderExist(to_folder)) {
									automation_stage = 1;
									System.out.println("+---------------------------{ automation stage "
											+ automation_stage + " }---------------------------+");
									Platform.runLater(new Runnable() {
										@Override
										public void run() {

											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).updateBar(automation_stage);
											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).setTheme();
										}
									});
								}
								break;
							case 1:
								try {
									service_connector.pushCommand(new HashMap<String, String>() {
										{
											put("type", "ai_request");
											put("from_folder_path", from_folder);
											put("to_folder_path", to_folder);
										}
									});
									automation_stage = 2;
									System.out.println("+---------------------------{ automation stage "
											+ automation_stage + " }---------------------------+");
									Platform.runLater(new Runnable() {
										@Override
										public void run() {

											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).updateBar(automation_stage);
											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).setTheme();
										}
									});
								} finally {
								}
								break;
							case 2:
								if (service_connector.isAi_started_analyzing_message()) {
									automation_stage = 3;
									System.out.println("+---------------------------{ automation stage "
											+ automation_stage + " }---------------------------+");
									Platform.runLater(new Runnable() {
										@Override
										public void run() {

											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).updateBar(automation_stage);
											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).setTheme();
										}
									});
									// ((ProgressController)
									// layout_components_controllers
									// .get(LayoutComponents.Progress)).updateBar(1);
								}
								// }
								break;
							case 3:
								if (service_connector.isFolder_empty_message()) {
									automation_stage = 0;
									System.out.println("+---------------------------{ automation stage "
											+ automation_stage + " }---------------------------+");
									Platform.runLater(new Runnable() {
										@Override
										public void run() {

											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).updateBar(automation_stage);
											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).setTheme();
										}
									});
								} else if (!service_connector.getAutomaticSortingDictionary().isEmpty()) {
									automation_stage = 4;
									System.out.println("+---------------------------{ automation stage "
											+ automation_stage + " }---------------------------+");
									Platform.runLater(new Runnable() {
										@Override
										public void run() {

											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).updateBar(automation_stage);
											((ProgressController) layout_components_controllers
													.get(LayoutComponents.Progress)).setTheme();
										}
									});
								}
								break;
							case 4:
								// Thread automatic_sorting_thread = new Thread(new Runnable() {
								// public void run() {
								// // Platform.runLater(new Runnable() {
								// // @Override
								// // public void run() {
								// //
								// // ((ProgressController)
								// // layout_components_controllers
								// // .get(LayoutComponents.Progress)).updateBar(2);
								// // ((ProgressController)
								// // layout_components_controllers
								// // .get(LayoutComponents.Progress)).setTheme();
								// // }
								// // });
								// automaticlySortFiles(service_connector.getAutomaticSortingDictionary());
								// service_connector.clearAutomaticSortingDictionary();
								// }
								// });
								// automatic_sorting_thread.start();
								// automation_stage = 0;

								automaticlySortFiles(service_connector.getAutomaticSortingDictionary());
								service_connector.clearAutomaticSortingDictionary();
								automation_stage = 0;

								System.out.println("+---------------------------{ automation stage " + automation_stage
										+ " }---------------------------+");
								Platform.runLater(new Runnable() {
									@Override
									public void run() {

										((ProgressController) layout_components_controllers
												.get(LayoutComponents.Progress)).updateBar(automation_stage);
										((ProgressController) layout_components_controllers
												.get(LayoutComponents.Progress)).setTheme();
									}
								});
								break;
							case 5:
								break;
							}
							lastUpdate = now;
						}
					}
				};
				timer.start();
				return null;
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	public HashMap<String, RedirectCellController> getRedirectCellControlers() {
		return redirect_cell_controlers;
	}

	public void updateRedirectCells(HashMap<String, String> redirects_dictionary) {
		this.redirect_cell_controlers.clear();
		this.redirect_cell_panes.clear();
		((PrimaryTabController) this.settings_tabs_controllers.get(SettingsTabs.Primary)).clearRedirectCells();
		FXMLLoader loader;
		for (String from_owner : redirects_dictionary.keySet()) {
			try {
				loader = new FXMLLoader(getClass().getResource("/Views/Screens/ScreensComponents/RedirectCell.fxml"));
				this.redirect_cell_panes.put(from_owner, loader.load());
				redirect_cell_controlers.put(from_owner, loader.getController());
				redirect_cell_controlers.get(from_owner).init(from_owner, redirects_dictionary.get(from_owner));
				redirect_cell_controlers.get(from_owner).setTheme();
				((PrimaryTabController) this.settings_tabs_controllers.get(SettingsTabs.Primary))
						.addRedirectCell(this.redirect_cell_panes.get(from_owner));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void redirect(String to_owner) {
		Thread send_thread = new Thread(new Runnable() {
			public void run() {
				System.out.println("=> redirect");
				HashMap<String, String> redirections = new HashMap<>();
				for (String from_owner : redirect_cell_controlers.keySet()) {
					if (redirect_cell_controlers.get(from_owner).isSelected()) {
						redirections.put(from_owner, redirect_cell_controlers.get(from_owner).getToOwner());
						System.out.println(from_owner + " " + redirect_cell_controlers.get(from_owner).getToOwner());
					}
				}
				JSONObject from_owners = new JSONObject(redirections);
				System.out.println(from_owners);
				service_connector.sendReDirectRequest(from_folder, from_owners.toString(), to_owner);
			}
		});
		send_thread.start();
	}

	public void login(String username, String password) {
		service_connector.pushCommand(new HashMap<String, String>() {
			{
				put("type", "login");
				put("username", username);
				put("password", password);
			}
		});
	}

	//
	// }
	// } catch (Exception e) {
	// }
	// }else
	//
	// {
	// try {
	// amount_of_files = 0;
	// ((ProgressController)
	// layout_components_controllers.get(LayoutComponents.Progress)).updateBar(0);
	// ((LayoutController)
	// layout_components_controllers.get(LayoutComponents.Layout)).setNoURL();
	// automation_lock_1 = true;
	// automation_lock_2 = false;
	// } catch (Exception e) {
	// }
	// }
	// System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
	// ((ProgressController)layout_components_controllers.get(LayoutComponents.Progress)).setTheme();lastUpdate=now;
	//
	// }}};timer.start();System.out.println("GG");return null;}};
	//
	// Thread th = new Thread(task);th.setDaemon(true);th.start();
	// }

}
