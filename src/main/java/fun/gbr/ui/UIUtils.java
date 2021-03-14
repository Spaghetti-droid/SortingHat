package fun.gbr.ui;

import java.util.Scanner;

/**
 * Useful methods for UIs
 *
 */
public class UIUtils {

//	/**
//	 * Sends prompt to user and reads input. Calls specific methods for certain
//	 * inputs
//	 * 
//	 * @param scanner
//	 * @param prompt
//	 * @return User response
//	 * @throws UserQuit
//	 */
//	public static String treatUserInput(Scanner scanner, String prompt) throws UserQuit {
//		
//		return treatUserInput(scanner, prompt, (String[]) null);
//		
//	}
	
	/**
	 * Sends prompt to user and reads input. Calls specific methods for certain
	 * inputs
	 * 
	 * @param scanner
	 * @param prompt
	 * @param expected Inputs considered valid for this prompt (disregarding special commands)
	 * @return User response
	 * @throws UserQuit
	 */
	public static String treatUserInput(Scanner scanner, String prompt, String... expected) throws UserQuit {
		
		if (scanner == null) {
			throw new IllegalArgumentException("scanner can't be null");
		}

		System.out.println(prompt);

		boolean getMoreInput = true;
		String reponse = null;
		while (getMoreInput) {
			reponse = scanner.nextLine();
			
			// Check for commands
			
			String trimmedResponse = reponse.trim();
			if (Screen.commandsAndScreens.containsKey(trimmedResponse)) {
				Screen.commandsAndScreens.get(trimmedResponse).open(scanner);
				System.out.println("\n"+prompt);
			} else if(checkUserResponse(trimmedResponse, expected)) {
				getMoreInput = false;
			} else {
				System.err.println("\nUnrecognised response: " + trimmedResponse);
				System.out.println("Please try again.");
				System.out.println("\n"+prompt);
			}
		}

		return reponse;		
	}
	
	/** Compares response to strings contained in expected. 
	 * @param reponse
	 * @param expected
	 * @return true if response is in expected, false otherwise
	 */
	private static boolean checkUserResponse(String reponse, String[] expected) {
		if(expected == null || expected.length == 0) {
			return true;
		}
		
		for(String acceptable: expected) {
			if(acceptable.equals(reponse)) {
				return true;
			}
		}		
		
		return false;
	}
}
