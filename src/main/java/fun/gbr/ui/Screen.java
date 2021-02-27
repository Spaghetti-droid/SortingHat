package fun.gbr.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Determines methods that need to be implemented by objects that represent a specific part of the user interface
 *
 */
public interface Screen {
	
	public static Map<String, Screen> commandsAndScreens = new HashMap<>();
	public static void initScreens() {
		Screen.commandsAndScreens.put("h", new HelpScreen());
		Screen.commandsAndScreens.put("o", new OptionsScreen());
		Screen.commandsAndScreens.put("q", new QuitScreen());
	}

	/** Triggers the screen usually allowing the user to interact with it in some way
	 * @param scanner
	 * @throws UserQuit
	 */
	public void open(Scanner scanner) throws UserQuit;
	
}
