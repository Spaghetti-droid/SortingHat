package fun.gbr.ui;

import java.util.Scanner;

public class UIUtils {
	
	public static String treatUserInput(Scanner scanner, String prompt) throws UserQuit {
		if(scanner == null) {
			throw new IllegalArgumentException("scanner can't be null");
		}
		
		System.out.println(prompt);
		
		boolean moreInput = true;
		String input = null;
		while(moreInput) {
			input = scanner.nextLine();
			if("h".equals(input) || input.equalsIgnoreCase("help")) {
				displayHelp();
				System.out.println(prompt);
			} else if("q".equals(input)) {
				throw new UserQuit();
			} else {
				moreInput = false;
			}
		}
		
		return input;	
	}
	
	private static void displayHelp() {
		System.out.println("This is a helpful message");
	}

}
