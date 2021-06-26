package Models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.json.JSONException;
import org.json.JSONObject;

import Controllers.Controller;
import Controllers.LayoutComponents.LayoutController;
import Controllers.LayoutComponents.LoginController;
import Controllers.Screens.NotificationsScreenController;
import Controllers.Screens.ScreensComponents.NetworkingTabController;

public class ServiceConnector {

	private Socket service_socket = null;
	private DataOutputStream out;
	private DataInputStream in;

	private HashMap<String, HashMap<String, String>> automatic_sorting_dictionary;

	private boolean ai_started_analyzing_message;
	private boolean folder_empty_message;

	private Model model;

	private Queue<HashMap<String, String>> command_queue;
	//private Stack<HashMap<String, String>> command_stack;
	private Stack<String> accepts_stack;

	private String username;
	private String password;

	// private String username ="test_user";
	public void nullifyStackAndQueue() {
		//this.command_stack.clear();
		this.command_queue.clear();
		this.accepts_stack.clear();
	}
	
	
	public ServiceConnector() {
		try {
			this.startSocket();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void startSocket() throws IOException {
		this.service_socket = new Socket(InetAddress.getByName("localhost"), 5500);
		System.out.println(this.service_socket);
		this.out = new DataOutputStream(this.service_socket.getOutputStream());
		System.out.println(this.out);
		this.in = new DataInputStream(this.service_socket.getInputStream());
		System.out.println("1_ " + this.in);
		System.out.println("2_ " + this.in.readUTF());
		this.service_socket.setSoTimeout(20);

		this.automatic_sorting_dictionary = new HashMap<String, HashMap<String, String>>();
		//this.command_stack = new Stack<HashMap<String, String>>();
		this.command_queue=new LinkedList<HashMap<String, String>>();
		this.accepts_stack = new Stack<String>();

		this.setUsername(null);
		this.setPassword(null);
	}

	public void close() throws IOException {
		this.in.close();
		this.out.close();
		this.service_socket.close();
	}

	public HashMap<String, HashMap<String, String>> getAutomaticSortingDictionary() {
		return this.automatic_sorting_dictionary;
	}

	public void clearAutomaticSortingDictionary() {
		this.automatic_sorting_dictionary.clear();
		;
	}

	// send
	// -------------------------------------------------------------------------------------
	public void pushAcceptRequest(String accept) {
		this.accepts_stack.push(accept);
		System.out.println("<.> PUSH A: " + accept);
	}

	public void popAcceptRequest(String accept) {
		if (this.accepts_stack.peek().equals(accept)) {
			this.accepts_stack.pop();
			System.out.println("<'> POP A: " + accept);
		}
	}

	public void pushCommand(HashMap<String, String> command) {
		//this.command_stack.push(command);
		this.command_queue.add(command);
		System.out.println("<.> PUSH C: " + command.get("type"));
	}

	public Queue<HashMap<String, String>> getCommandStack() {
		//return this.command_stack;
		return this.command_queue;
	};

	public Stack<String> getAcceptsStack() {
		return this.accepts_stack;
	};

	public String sendCommand() {
		//if (!command_stack.isEmpty()) {
		if (!command_queue.isEmpty()) {
			if (accepts_stack.isEmpty()) {
				//HashMap<String, String> command = command_stack.pop();
				HashMap<String, String> command = command_queue.poll();
				System.out.println("<'> POP C: " + command.get("type"));
				switch (command.get("type")) {
				case "ai_request":
					sendAIRequest(command.get("from_folder_path"), command.get("to_folder_path"));
					pushAcceptRequest("AI ended analyzing");
					pushAcceptRequest("AI started analyzing");
					return "send commend ai_request";
				case "set_from_folder":
					sendSetFromFolder(command.get("path"));
					pushAcceptRequest("from_folder updated in the data-base");
					return "send commend set_from_folder";
				case "set_to_folder":
					sendSetToFolder(command.get("path"));
					pushAcceptRequest("to_folder updated in the data-base");
					return "send commend set_to_folder";
				case "move_file":
					sendMoveFileRequest(command.get("file_name"), command.get("from_folder_path"),
							command.get("to_folder_path"), command.get("owner"), command.get("label"),
							command.get("way_of_sorting"));
					pushAcceptRequest("file has been updated in the database");
					return "send commend move_file";
				case "login":
					sendLogin(command.get("username"), command.get("password"));
					pushAcceptRequest("login");
					return "send commend login";

					
				case "redirection_list_request":
					sendRedirectionListRequest(command.get("to_folder_path"));
					pushAcceptRequest("redirections were sent");
					return "send commend redirection_list_request";	
						
				default:
					return ("problem with " + command.toString());
				}

			} else
				return ("waiting for " + accepts_stack.peek());
		} else
			return ("no command");
	}

	public String sendConnectionTest(String username, String password) {
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "connection_test");
		message_hash_map.put("username", username);
		message_hash_map.put("password", password);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendAIRequest(String from_folder_path, String to_folder_path) {
		System.out.println("send commend ai_request");
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "ai_request");
		message_hash_map.put("username", username);
		message_hash_map.put("from_folder_path", from_folder_path);
		message_hash_map.put("to_folder_path", to_folder_path);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendMoveFileRequest(String file_name, String from_folder_path, String to_folder_path, String owner,
			String label, String way_of_sorting) {
		System.out.println("send commend move_file");
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "move_file");
		message_hash_map.put("username", username);
		message_hash_map.put("file_name", file_name);
		message_hash_map.put("from_folder_path", from_folder_path);
		message_hash_map.put("to_folder_path", to_folder_path);
		message_hash_map.put("owner", owner);
		message_hash_map.put("label", label);
		message_hash_map.put("way_of_sorting", way_of_sorting);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendSetFromFolder(String path) {
		System.out.println("send commend set_from_folder");
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "set_from_folder");
		message_hash_map.put("username", username);
		message_hash_map.put("path", path);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendSetToFolder(String path) {
		System.out.println("send commend set_to_folder");
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "set_to_folder");
		message_hash_map.put("username", username);
		message_hash_map.put("path", path);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendLogin(String username, String password) {
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "login");
		message_hash_map.put("username", username);
		message_hash_map.put("password", password);

		System.out.println(message_hash_map.toString());
		System.out.println(new JSONObject(message_hash_map));
		System.out.println(new JSONObject(message_hash_map).get("username"));
		String message = new JSONObject(message_hash_map).toString();
		System.out.println(message);
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendLogout(String username, String password) {
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "logout");
		message_hash_map.put("username", username);
		message_hash_map.put("password", password);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				System.out.println(e);
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			System.out.println(e);
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendRemoveFilesRequest(String from_folder_path, String file_name, String location) {
		System.out.println("send commend remove_file");
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "remove_file");
		message_hash_map.put("username", username);
		message_hash_map.put("from_folder_path", from_folder_path);
		message_hash_map.put("file_name", file_name);
		message_hash_map.put("location", location);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendUnremoveFileRequest(String from_folder_path, String file_name) {
		System.out.println("send commend unremove_file");
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "unremove_file");
		message_hash_map.put("username", username);
		message_hash_map.put("from_folder_path", from_folder_path);
		message_hash_map.put("file_name", file_name);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendReDirectRequest(String to_folder_path, String from_owners, String to_owner) {
		System.out.println("send commend redirect");
		System.out.println(from_owners);
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("to_folder_path", to_folder_path);
		message_hash_map.put("type", "redirect");
		message_hash_map.put("username", username);
		// message_hash_map.put("from_owners", from_owners);
		message_hash_map.put("from_owners", "banna");
		message_hash_map.put("to_owner", to_owner);

		String message = new JSONObject(message_hash_map).toString();
		System.out.println(message);
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendRemovedFilesListRequest(String from_folder_path) {
		System.out.println("send commend removed_files_list_request");
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "removed_files_list_request");
		message_hash_map.put("username", username);
		message_hash_map.put("from_folder_path", from_folder_path);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	public String sendRedirectionListRequest(String to_folder_path) {
		System.out.println("send commend redirection_list_request");
		HashMap<String, String> message_hash_map = new HashMap<String, String>();
		message_hash_map.put("type", "redirection_list_request");
		message_hash_map.put("username", username);
		message_hash_map.put("to_folder_path", to_folder_path);

		String message = new JSONObject(message_hash_map).toString();
		if (this.service_socket == null) {
			try {
				this.startSocket();
			} catch (Exception e) {
				return ("Error: cannot find the service, please open it");
			}
		}
		try {
			this.out.writeUTF(message);
			this.out.flush();
		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
		return ("commend was sent successfully");
	}

	// receive
	// -------------------------------------------------------------------------------------
	public String receive() {
		try {
			String message = this.in.readUTF();
			System.out.println("received");
			message = (message.contains("{")) ? this.handleJSONMessages(message) : this.handleStringMessages(message);
			return message;

		} catch (Exception e) {
			return ("Error: faulty connection to service, please try agin");
		}
	}

	public String handleStringMessages(String message) {
		switch (message) {
		case "AI started analyzing":
			popAcceptRequest("AI started analyzing");
			this.ai_started_analyzing_message = true;
			break;
		case "Folder Empty":
			popAcceptRequest("AI ended analyzing");
			((NotificationsScreenController) this.model.getScreenControllers().get(Screens.Notifications)).addNotifications("AI Problem");
			this.folder_empty_message = true;
			this.model.setAutomationStage(-1);
			break;
		case "AI exception occurred":
			popAcceptRequest("AI ended analyzing");
			((NotificationsScreenController) this.model.getScreenControllers().get(Screens.Notifications)).addNotifications("AI Problem");
			this.folder_empty_message = true;
			this.model.setAutomationStage(-1);
			break;
		case "from_folder updated in the data-base":
			popAcceptRequest("from_folder updated in the data-base");
			break;
		case "to_folder updated in the data-base":
			popAcceptRequest("to_folder updated in the data-base");
			break;
		case "file has been updated in the database":
			popAcceptRequest("file has been updated in the database");
			break;
		case "Error: You have already opened as many windows as you are allowed":
			((LoginController) this.model.getLayoutComponentsControllers().get(LayoutComponents.Login))
					.setErorrText(message);
			popAcceptRequest("login");
			break;
		case "Error: Wrong password, Sorry":
			((LoginController) this.model.getLayoutComponentsControllers().get(LayoutComponents.Login))
					.setErorrText(message);
			popAcceptRequest("login");
			break;
		case "Error: The name was not found, please recheck and try again":
			((LoginController) this.model.getLayoutComponentsControllers().get(LayoutComponents.Login))
					.setErorrText(message);
			popAcceptRequest("login");
			break;
		default:
			break;
		}
		return message;
	}

	public String handleJSONMessages(String message) {
		System.out.println("-> " + message);
		System.out.println("שלום");
		try {
			JSONObject message_json = new JSONObject(message);
			switch (((Iterator<String>) message_json.keys()).next()) { // first-key
			case "AI_ended_analyzing":
				popAcceptRequest("AI ended analyzing");
				message = updateAutomaticSortingDictionary(((JSONObject) message_json.get("AI_ended_analyzing")));
				break;
			case "login":
				popAcceptRequest("login");
				message = settingUser(((JSONObject) message_json.get("login")));
				break;

			// case "removed_files":
			// message = updateAutomaticSortingDictionary(((JSONObject) message_json.get("AI
			// ended analyzing")));
			// break;
			case "redirections":
				popAcceptRequest("redirections were sent");
				message = updateRedirections(((JSONObject) message_json.get("redirections")));
				break;
			// case "redirection_files":
			// message = updateAutomaticSortingDictionary(((JSONObject) message_json.get("AI
			// ended analyzing")));
			// break;
			// case "login":
			// message = updateAutomaticSortingDictionary(((JSONObject) message_json.get("AI
			// ended analyzing")));
			// break;
			// default:
			// break;
			}
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: handle JSON Messages ";
		}
	}

	// update
	// -------------------------------------------------------------------------------------

	private String settingUser(JSONObject info_json) {
		System.out.println("settingUser");
		System.out.println(info_json);

		this.username = info_json.getString("username");
		System.out.println(username);
		((NetworkingTabController) this.model.getSettingsTabsControllers().get(SettingsTabs.Networking))
		.setUserName(info_json.getString("username"));
		
		this.password = info_json.getString("password");
		((NetworkingTabController) this.model.getSettingsTabsControllers().get(SettingsTabs.Networking))
				.setUserImage(info_json.getString("image_url"));
		
		if(info_json.has("last_from_folder"))
			this.model.setFromFolder(info_json.getString("last_from_folder"));
		
		if(info_json.has("last_to_folder"))
			this.model.setToFolder(info_json.getString("last_to_folder"));
		
		((LayoutController) this.model.getLayoutComponentsControllers().get(LayoutComponents.Layout)).closeLoginScreen();
		
		return "finished logging in";
	}

	public String updateAutomaticSortingDictionary(JSONObject automatic_sorting_dictionary_JSON) {
		System.out.println("updateAutomaticSortingDictionary");
		this.automatic_sorting_dictionary = new HashMap<String, HashMap<String, String>>();
		for (final Iterator<String> iterator_folder = automatic_sorting_dictionary_JSON.keys(); iterator_folder
				.hasNext();) {
			final String file_title = iterator_folder.next();
			HashMap<String, String> file_info_dictionary = new HashMap<String, String>();
			for (final Iterator<String> iter_info = ((JSONObject) automatic_sorting_dictionary_JSON.get(file_title))
					.keys(); iter_info.hasNext();) {
				final String info_title = iter_info.next();
				file_info_dictionary.put(info_title,
						((JSONObject) automatic_sorting_dictionary_JSON.get(file_title)).getString(info_title));
				System.out.println(info_title + ": " + file_info_dictionary.get(info_title));
			}
			this.automatic_sorting_dictionary.put(file_title, file_info_dictionary);
			System.out.println(file_title + "= " + this.automatic_sorting_dictionary.get(file_title));
		}
		return "Automatic Sorting ended";
	}

	public String updateRedirections(JSONObject redirects_dictionary_JSON) {
		System.out.println("updateRedirections");
		System.out.println(redirects_dictionary_JSON);
		HashMap<String, String> redirects_dictionary = new HashMap<String, String>();
		for (final Iterator<String> iter_info = redirects_dictionary_JSON.keys(); iter_info.hasNext();) {
			final String info_title = iter_info.next();
			redirects_dictionary.put(info_title, redirects_dictionary_JSON.getString(info_title));
			System.out.println(info_title + ": " + redirects_dictionary.get(info_title));
		}
		this.model.updateRedirectCells(redirects_dictionary);
		return "finshed updating redirections";

	}

	public boolean isAi_started_analyzing_message() {
		return ai_started_analyzing_message;
	}

	public void nullifyAi_started_analyzing_message() {
		this.ai_started_analyzing_message = false;
	}

	public boolean isFolder_empty_message() {
		return folder_empty_message;
	}

	public void nullifyFolder_empty_message() {
		this.folder_empty_message = false;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}