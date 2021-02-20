package fun.gbr.ui;

import static fun.gbr.ui.UIUtils.treatUserInput;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import fun.gbr.entity.AddressInfo;
import fun.gbr.parameters.FilePurpose;
import fun.gbr.service.ListService;

//TODO Handle windows \ syntax
//TODO Test specified paths
//TODO consider light version without dependencies

/**
 * Launches Sorting hat
 *
 */
public class LaunchSort {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.println(
				"Hey, welcome to Sorting hat!\n"
				+ "Shuffle a list from a file or the terminal :)\n"
				+ "Type h at any prompt to get more info, q to quit.\n");

		try {

			while (true) {

				ListService service = new ListService();

				try {

					// Get file address for read, if there is one, and check input

					boolean choiceGood = false;
					while (!choiceGood) {
						String readChoice = treatUserInput(scanner, "Read from file? (y/n/address)");
						choiceGood = checkChoice(readChoice, FilePurpose.READ, scanner, service);
					}

					// Get write address and check

					choiceGood = false;
					while (!choiceGood) {
						String writeChoice = treatUserInput(scanner, "Write to file? (y/n/address)");
						choiceGood = checkChoice(writeChoice, FilePurpose.WRITE, scanner, service);
					}

					// Based on user choices, instantiate fetcher and writer

					service.initWriterAndFetcher(scanner);

					// Do shuffle loop

					String userConfirmed = treatUserInput(scanner, "Parameters set. Are you happy with them? (y/n)");
					boolean proceed = "y".equals(userConfirmed);
					while (proceed) {

						service.fetchShuffleAndWrite();

						String contChoice = treatUserInput(scanner, "Shuffle again with the same parameters? (y/n)");
						if (!"y".equals(contChoice)) {
							proceed = false;
						}
					}
				} catch (Exception e) {
					System.err.println("Error occured:");
					e.printStackTrace();
				}

			}

		} catch (UserQuit uq) {

			System.out.println("Alright, see ya!");

		} finally {
			scanner.close();
		}

	}

	/**
	 * Checks user choice for the read/write file locations. Includes verification
	 * of file existence and user confirmed creation
	 * 
	 * @param choice  y/n/address
	 * @param purpose Identifies if file is input or output
	 * @param scanner
	 * @param service The list service
	 * @return true if choice valid
	 * @throws UserQuit If user quit
	 */
	private static boolean checkChoice(String choice, FilePurpose purpose, Scanner scanner, ListService service)
			throws UserQuit {

		choice = choice.trim();
		AddressInfo info = initStorageHandler(choice, purpose, service);
		if (info == null) {
			return true;
		}

		// If default file, confirm with user

		if (info.isDesiredFileGuessed()) {
			String response = treatUserInput(scanner,
					"File at " + service.getFile(purpose).getPath() + " will be used. Is this correct? (y/n)");
			if (!"y".equals(response)) {
				return false;
			}
		}

		// Analyse valid files if != 1 and potentially prompt user to create or select
		// one.

		int validFiles = reportChoice(info, purpose, scanner, service);
		if (validFiles != 1) {
			service.clearHandler(purpose);
		}
		return validFiles == 1;
	}

	/**
	 * Analyses info about address and asks user for input if file needs to be
	 * created
	 * 
	 * @param info    contains info about the input address
	 * @param purpose Identifies if file is input or output
	 * @param scanner
	 * @param service
	 * @return Number of valid files found
	 * @throws UserQuit
	 */
	private static int reportChoice(AddressInfo info, FilePurpose purpose, Scanner scanner, ListService service)
			throws UserQuit {

		// Get info
		boolean isDir = info.isDirAddress();
		int validFiles = info.getValidFileNumberAtAddress();
		String userResponse = null;

		// If no valid files

		if (validFiles == 0) {
			if (isDir) {
				System.out.println("The directory " + info.getDirectory() + " doesn't contain any valid csv files.");
			} else {
				System.out.println(
						"The File " + info.getAddress() + " doesn't seem to exist or is not a valid csv file.");
			}

			// If it's the output file, offer creation

			if (FilePurpose.WRITE.equals(purpose) && scanner != null) {
				userResponse = treatUserInput(scanner, "Would you like to create a new csv file? (y/n)");
				if ("y".equals(userResponse)) {
					boolean success = false;
					if (isDir) {
						userResponse = treatUserInput(scanner, "What name?");
						success = service.createOutputFileInDirectory(userResponse);
					} else {
						success = service.createOutputFile();
					}
					if (success) {
						validFiles = 1;
					} else {
						System.out.println("Couldn't create new file!");
					}
				}
			}

			// If too many valid files, notify user

		} else if (validFiles > 1) {
			List<File> validFileList = service.getvalidFileList(purpose);
			
			File chosenFile = null;
			boolean retry = true;
			while(retry) {
				chosenFile = chooseValidFile(validFiles, validFileList, info, service, purpose, scanner);
				String prompt = null;
				if(chosenFile == null) {
					prompt = "You do not want to use any of the available files. Is this correct? (y/n)";
				} else {
					prompt = "You have chosen " + chosenFile.getPath() + ". Is this correct? (y/n)";
				}
				userResponse = treatUserInput(scanner, prompt);
				retry = !"y".equalsIgnoreCase(userResponse);
			}
			if(chosenFile != null) {
				validFiles = 1;
			}

		}

		return validFiles;
	}

	/** Lets the user choose a file from a selection
	 * @param validFiles
	 * @param validFileList
	 * @param info
	 * @param service
	 * @param purpose
	 * @param scanner
	 * @return The chosen file or null if non chosen
	 * @throws UserQuit
	 */
	private static File chooseValidFile(int validFiles, List<File> validFileList, AddressInfo info, ListService service, FilePurpose purpose, Scanner scanner) throws UserQuit {

		System.out.println("Found " + validFiles + " potentially valid files in " + info.getDirectory()
				+ ". Please specify one csv file: ");

		String prompt = "";

		int i = 1;
		for (File file : validFileList) {
			prompt += i + ". " + file.getName() + "\n";
			i++;
		}
		prompt += i + ". (or any other input) None of the above.";
		String userResponse = treatUserInput(scanner, prompt);
		int userchoice = -1;
		try {
			userchoice = Integer.valueOf(userResponse) - 1;
		} catch (NumberFormatException e) {
			// keep user choice at -1
		}
		if (userchoice > -1 && userchoice < validFiles) {
			service.setFile(validFileList.get(userchoice), purpose);
			return validFileList.get(userchoice);
		}
		
		return null;
	}

	/**
	 * Adds fileHandler to service for input or output
	 * 
	 * @param choice  y/n/address
	 * @param purpose read or write
	 * @param service
	 * @return info about the file address
	 */
	private static AddressInfo initStorageHandler(String choice, FilePurpose purpose, ListService service) {

		if ("n".equals(choice)) {
			return null;
		}
		String address = null;
		if (!"y".equals(choice)) {
			address = choice;
		}

		return service.setFileHandler(address, purpose);
	}

}
