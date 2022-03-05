package fun.gbr.parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Accesses and stores options, such as default parameter values
 *
 * I had a bit of fun with reflection here, but as a result there must be some
 * standardisation in the name of parameters! The rules are: 
 * - All lists of extensions finish with ESTANSIONS 
 * - All paths finish with LOCATION 
 * - All Names finish with NAME
 */
public abstract class Options {

	// Parameters that govern the behaviour of option setting

	private static final File OPTIONS_FILE = new File("./Options.txt");
	private static final Set<String> ILLEGAL_IN_EXTENSION = new HashSet<>(Arrays.asList(".", " "));
	private static final Set<String> NULL_INPUT_PATTERNS = new HashSet<>(Arrays.asList("", "NONE", "NULL", "UNSET")); // empty string never happens, but it probably should

	// Options with generic or name-based checks when setting

	public static String DEFAULT_INPUT_LOCATION = "./input/";
	public static String DEFAULT_OUTPUT_LOCATION = "./output/";
	public static String DEFAULT_FILE_NAME = "list.csv";

	public static Set<String> CSV_FILE_EXTENSIONS = new HashSet<>(Arrays.asList("csv", "txt"));

	// Options with specific checks when setting

	public static Integer MAX_REPEATS = Constants.INT_NONE_VALUE;

	/**
	 * Fetches options in file and sets them
	 */
	public static void initFromFile() {

		Field[] fields = Options.class.getFields();
		Set<String> optionNames = new HashSet<>(fields.length);
		for (Field field : fields) {

			optionNames.add(field.getName());

		}

		try (Scanner scanner = new Scanner(OPTIONS_FILE)) {

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				line = removeCommentsAndSpaces(line);
				if (line.isEmpty()) {
					continue;
				}

				List<String> nameAndValue = getOptionNameAndValue(line);
				String name = nameAndValue.get(0);
				String value = nameAndValue.get(1);
				if (!optionNames.contains(name)) {
					continue;
				}

				changeOptionValue(name, value);
			}

		} catch (FileNotFoundException e) {

			System.err.println("Couldn't find 'Options.txt' file! Keeping to default options.");

		}
	}

	/** Strips line of comments (starting with #) and leading or trailing spaces
	 * @param line
	 * @return
	 */
	private static String removeCommentsAndSpaces(String line) {

		if (line.isEmpty() || line.startsWith("#")) {
			return "";
		}
		int commentIdx = line.indexOf("#");
		if (commentIdx != -1) {
			line = line.substring(0, commentIdx);
		}
		return line.trim();
	}

	/** Parses line to obtain the name and value of the option to be modified 
	 * @param line
	 * @return the name and value
	 */
	private static List<String> getOptionNameAndValue(String line) {

		int columnIdx = line.indexOf(":");
		if (columnIdx == -1) {
			return new ArrayList<>(0);
		}

		List<String> nameAndValue = new ArrayList<>(2);
		nameAndValue.add(line.substring(0, columnIdx).trim());
		nameAndValue.add(line.substring(columnIdx+1).trim());

		return nameAndValue;
	}

	/** Sets an input value to an option designated by name
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void changeOptionValue(String fieldName, String fieldValue){

		if (fieldValue.isEmpty()) {
			System.out.println("No value specified for "+fieldName+". Keeping default");
			return;
		}

		try {

			Field field = Options.class.getField(fieldName);
			changeOptionValue(field, fieldValue);

		} catch (Exception e) {
			System.err.println("Unable to set "+fieldName+" due to exception!");
			e.printStackTrace();
		} 

	}
	
	/**  Sets an input value to an option directly using the field object
	 * @param field
	 * @param fieldValue
	 * @return true if setting was successful, false otherwise
	 */
	public static boolean changeOptionValue(Field field, String fieldValue) {
		
		try {

			Class<?> fieldClass = field.getType();
			String fieldName = field.getName();
			if (fieldClass.equals(Set.class)) {

				Set<String> fieldValueSet = new HashSet<>(Arrays.asList(fieldValue.split(",")));
				fieldValueSet = fieldValueSet.stream().map(v -> v=v.trim()).collect(Collectors.toSet());
				passGenericTests(fieldName, fieldValueSet);
				field.set(null, fieldValueSet);

			} else if (fieldClass.equals(String.class)) {

				field.set(null, fieldValue);

			} else if (fieldClass.equals(int.class) || fieldClass.equals(Integer.class)) {

				Integer intValue = handleInputInt(fieldValue);
				if(!passSpecialChecks(fieldName, intValue)) {
					return false;
				}				
				field.set(null, intValue);

			} else {

				System.out.println("Don't recognise field type. Trying to set with cast!");
				field.set(null, fieldClass.cast(fieldValue));

			}

			return true;

		}catch(IllegalAccessException e) {
			System.err.println("Unable to set "+field.getName()+". Illegal access.");
			e.printStackTrace();
			return false;
		}catch(NumberFormatException e) {
			System.out.println(fieldValue + " is not a valid value for " + field.getName());
			return false;
		}
	}
	
	/** Converts options input into an int value
	 * @param fieldValue
	 * @return int value, or the INT_NONE_VALUE from constants
	 */
	private static Integer handleInputInt(String fieldValue) {
		
		Integer intValue = Constants.INT_NONE_VALUE;
		if(!NULL_INPUT_PATTERNS.contains(fieldValue.toUpperCase())) {
			intValue = Integer.valueOf(fieldValue);
		}
		
		return intValue;		
	}
	
	/** Check requirements that are specific to one field
	 * @param fieldName
	 * @param fieldValue
	 * @return true if the value passes requirements
	 */
	private static boolean passSpecialChecks(String fieldName,Integer fieldValue) {
		
		if ("MAX_REPEATS".equals(fieldName)) {
			if (fieldValue != Constants.INT_NONE_VALUE && fieldValue <1) {
				System.err.println(fieldValue + " is not a valid maximum number of repeats!");
				return false;
			}
			return true;	
		}
		
		return true;		
	}
	
	/** Checks if value is valid for field. Uses patterns in the option name to determine conditions
	 * @param fieldName
	 * @param fieldValueSet
	 * @return true if value passes conditions
	 */
	private static boolean passGenericTests(String fieldName, Set<String> fieldValueSet) {
		
		if (fieldName.endsWith("EXTENSIONS")) {
			for (String value : fieldValueSet) {
				for (String illegalChars : ILLEGAL_IN_EXTENSION) {
					if (value.contains(illegalChars)) {
						System.err.println(fieldName + " not set: Illegal characters in extensions!");
						return false;
					}
				}
			}
			return true;
		}
		
		return true;		
	}
	
	// %%%%%%%%%% Interface methods %%%%%%%%%%
	
	public static int getMaxRepeats() {
		if(MAX_REPEATS == Constants.INT_NONE_VALUE) {
			return 0;
		}
		return MAX_REPEATS;
	}
	
	public static void setMaxRepeats(Integer repeats) {
			if(repeats == 0) {
				MAX_REPEATS = Constants.INT_NONE_VALUE;
			} else if(repeats>0) {
				MAX_REPEATS = repeats;
			} else {
				throw new IllegalArgumentException("Invalid value for Max Repeats: " + repeats); //TODO the message shouldn't be set here
			}		
	}
}
