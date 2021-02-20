package fun.gbr.ui;

import java.util.Scanner;

/**
 * Useful methods for UIs
 *
 */
public class UIUtils {

	private static String helpMessage = "\n\n~~~~~~~~~~~~~~~\n\n"
			+ "Help:\n"
			+ "This is an app that allows you to randomise the order of an entered list of elements.\n" + "\n"
			+ "Read and Write files:\n "
			+ "Sorting hat will ask you for files to read and/or write to. Your options are:\n"
			+ " -'n': Do not use a file for this, use terminal instead.\n"
			+ " -'y': Use the default file or a file in the default location. If a single file is found in the default location it'll be used. Otherwise, it'll default to 'list.csv'. If 'list.csv' doesn't exist, you'll be asked to specify one file.\n"
			+ " - Anything else: will be analysed as a path to a file.\n "
			+ "Valid file formats are .csv and .txt for the moment. Sorting hat only determines mime type by file extension, "
			+ "so these extensions need to be in the file name for the app to recognise them as readable/writable files.\n"
			+ "If an output file doesn't exist Sorting hat will offer to create it.\n"
			+ "\n" + "Useful commands: \n" + " -'q': Use at any prompt to quit\n" + " -'h': opens this help screen\n"
			+ "\n~~~~~~~~~~~~~~~\n";

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

		boolean moreInput = true;
		String input = null;
		while (moreInput) {
			input = scanner.nextLine();
			if ("h".equals(input) || input.equalsIgnoreCase("help")) {
				displayHelp();
				System.out.println(prompt);
			} else if ("q".equals(input)) {
				throw new UserQuit();
			} else {
				moreInput = false;
			}
		}

		return input;
	}

	/**
	 * Displays a help message
	 */
	private static void displayHelp() {
		System.out.println(helpMessage);
	}

}
