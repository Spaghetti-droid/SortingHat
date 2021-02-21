package fun.gbr.ui;

import java.lang.reflect.Field;

import fun.gbr.parameters.Options;

public class OptionsScreen implements Screen {

	@Override
	public void open() {
		
		displayMessage();
		
		// Suggest change here
	}
	
	private static void displayMessage() {
		
		System.out.println("These are the options being currently applied by Sorting hat:\n");
		for(Field field:Options.class.getFields()) {			
			try {
				System.out.println(field.getName() +": "+ field.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.err.println("Couldn't get option value for " + field.getName());
				e.printStackTrace();
			}
		}
		
	}
}
