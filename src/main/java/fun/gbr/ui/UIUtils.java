package fun.gbr.ui;

import java.util.Scanner;

/**
 * Useful methods for UIs
 *
 */
public class UIUtils {
	
	//TODO Could make treatUserInput take a list of expected responses, so that it can ask again if need be

	/**
	 * Sends prompt to user and reads input. Calls specific methods for certain
	 * inputs
	 * 
	 * @param scanner
	 * @param prompt
	 * @return User response
	 * @throws UserQuit
	 */
	public static String treatUserInput(Scanner scanner, String prompt) throws UserQuit {
		if (scanner == null) {
			throw new IllegalArgumentException("scanner can't be null");
		}

		System.out.println(prompt);

		boolean getMoreInput = true;
		String input = null;
		while (getMoreInput) {
			input = scanner.nextLine();
			
			// Check for commands
			
			String trimmedInput = input.trim();
			if (Screen.commandsAndScreens.containsKey(trimmedInput)) {
				Screen.commandsAndScreens.get(trimmedInput).open(scanner);
				System.out.println("\n"+prompt);
			} else {
				getMoreInput = false;
			}
		}

		return input;
	}
}
