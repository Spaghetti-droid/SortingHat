package fun.gbr.ui;

import java.util.HashMap;
import java.util.Map;

public interface Screen {
	
	public static Map<String, Screen> commandsAndScreens = new HashMap<>();
	public static void initScreens() {
		Screen.commandsAndScreens.put("h", new HelpScreen());
		Screen.commandsAndScreens.put("o", new OptionsScreen());
		Screen.commandsAndScreens.put("q", new QuitScreen());
	}

	public void open() throws UserQuit;
	
}
