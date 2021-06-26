package Views;

public class ThemeManager {
	private static String background_mainly="rgba(0, 0, 0, 0.7)";
	private static String background_secondary="rgba(0, 0, 0, 0.8)";
	
	private static String background_right_gradient="linear-gradient(to right, rgba(0, 0, 0,0), rgba(0, 0, 0, 0.8))";
	private static String background_bottom_gradient="linear-gradient(to bottom, rgba(0, 0, 0,0), rgba(0, 0, 0, 0.8))";
	
	//-----------------------------------------------------------------------------------------------------------------------
	
	private static String neutral_mainly="white";
	private static String neutral_secondary="rgba(83,87,98,1)";
	
	//-----------------------------------------------------------------------------------------------------------------------
	
	private static String settings_mainly="#ea4335";
	private static String settings_secondary="#ea4335";
	
	private static String view_and_edit_mainly="#fbbc05";
	private static String view_and_edit_secondary="#fbbc05";
	
	private static String notifications_mainly="#4271f4";
	private static String notifications_secondary="#4271f4";
	
	private static String current_mainly=notifications_mainly;
	private static String current_secondary=notifications_mainly;
	
	//-----------------------------------------------------------------------------------------------------------------------
	
	public static String backgroundMainly() {
		return background_mainly;
	}
	
	public static String backgroundSecondary() {
		return background_secondary;
	}
	
	public static String backgroundRightGradient() {
		return background_right_gradient;
	}
	
	public static String backgroundBottomGradient() {
		return background_bottom_gradient;
	}

	//-----------------------------------------------------------------------------------------------------------------------
	public static String neutralMainly() {
		return neutral_mainly;
	}
	
	public static String neutralSecondary() {
		return neutral_secondary;
	}
	
	public static String currentMainly() {
		return current_mainly;
	}
	

	

}
