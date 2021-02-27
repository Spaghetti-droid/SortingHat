package fun.gbr.ui;

import java.lang.reflect.Field;
import java.util.Scanner;

import fun.gbr.parameters.Options;

/**
 * The screen that handles viewing and setting options
 *
 */
public class OptionsScreen implements Screen {

	@Override
	public void open(Scanner scanner) throws UserQuit {
		
		boolean done = false;
		while(!done) {

			Field[] fields = Options.class.getFields();
			displayMessage(fields);

			// Suggest change here

			editOption(scanner, fields);
			String response = UIUtils.treatUserInput(scanner, "Done?(y/n)");
			done = "y".equals(response);
		}
	}
	
	/** Displays the options and their values
	 * @param fields
	 */
	private static void displayMessage(Field[] fields) {
		
		System.out.println("These are the options being currently applied by Sorting hat:\n");
		int i=1;
		for(Field field:fields) {			
			try {
				System.out.println(i++ +". "+ field.getName() +": "+ field.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.err.println("Couldn't get option value for " + field.getName());
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	/** Allows the user to edit the options used at runtime
	 * @param scanner
	 * @param fields
	 * @throws UserQuit
	 */
	private static void editOption(Scanner scanner, Field[] fields) throws UserQuit {
		
		String response = UIUtils.treatUserInput(scanner, "Input the option's number to edit its value, anything else to proceed without editing.");
		Integer choice = null;
		try {
			choice = Integer.valueOf(response)-1;
		} catch(NumberFormatException e) {
			// Leave choice null
		}
		
		if(choice == null || choice<0 || choice>fields.length) {
			return;
		}
		
		Field field = fields[choice];
		response = UIUtils.treatUserInput(scanner, "Input the new value.");

		if(Options.changeOptionValue(field, response)) {
			System.out.println(field.getName() + " = " + response);
		} else {
			System.out.println("Couldn't change option.");
		}
	}
}
