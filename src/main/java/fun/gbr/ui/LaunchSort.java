package fun.gbr.ui;

import static fun.gbr.ui.UIUtils.treatUserInput;

import java.util.Scanner;

import fun.gbr.entity.AddressInfo;
import fun.gbr.parameters.FilePurpose;
import fun.gbr.service.ListService;

//TODO Make writer go to empty lines to write 

public class LaunchSort {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.println(
				"Hey! This app can take inputs and outputs to and from files and the terminal. Type h at any prompt to get more info, q to quit.");
		System.out.println("\n%%%%%%%%%%%%%%%%%%%%\n");

		try {

			while (true) {

				ListService service = new ListService();

				try {

					// Read and check input

					boolean choiceGood = false;
					while (!choiceGood) {
						String readChoice = treatUserInput(scanner, "Read from file? (y/n/address)");
						choiceGood = checkChoice(readChoice, FilePurpose.READ, null, service);
					}

					// Write and check input

					choiceGood = false;
					while (!choiceGood) {
						String writeChoice = treatUserInput(scanner, "Write to file? (y/n/address)");
						choiceGood = checkChoice(writeChoice, FilePurpose.WRITE, scanner, service);
					}

					// Based on user choices, instanciate fetcher and writer

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

	private static boolean checkChoice(String choice, FilePurpose purpose, Scanner scanner, ListService service)
			throws UserQuit {

		AddressInfo info = initStorageHandler(choice, purpose, service);
		if (info == null) {
			return true;
		}

		int validFiles = reportChoice(info, purpose, scanner, service);
		if (validFiles != 1) {
			service.clearHandler(purpose);
		}
		return validFiles == 1;
	}

	private static int reportChoice(AddressInfo info, FilePurpose purpose, Scanner scanner, ListService service)
			throws UserQuit {

		boolean isDir = info.isDirAddress();
		int validFiles = info.getValidFileNumberAtAddress();
		String userResponse = null;

		if (validFiles == 0) {
			if (isDir) {
				System.out.println("The directory " + info.getDirectory() + " doesn't contain any valid csv files.");
			} else {
				System.out.println(
						"The File " + info.getAddress() + " doesn't seem to exist or is not a valid csv file.");
			}
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

		} else if (validFiles > 1) {
			System.out.println("Found " + validFiles + " potentially valid files in " + info.getDirectory()
					+ ". Please specify one csv file");
		}

		return validFiles;
	}

	private static AddressInfo initStorageHandler(String choice, FilePurpose purpose, ListService service) {

		choice = choice.trim();

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
