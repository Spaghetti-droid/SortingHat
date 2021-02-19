package fun.gbr.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.gbr.filter.CSVFilter;
import fun.gbr.parameters.Options;

public class FileHandler {

	private File file;
	private File directory;
	private boolean dirGiven;

	public FileHandler(String address) {
		super();
		File tempFile = new File(address);
		this.dirGiven = tempFile.isDirectory();
		if (this.dirGiven) {
			this.directory = new File(address);
		} else {
			this.file = new File(address);
			this.directory = this.file.getParentFile();
		}
	}

	/**
	 * If StorageHandler was constructed with a dir address, looks for a csv file
	 * contained in it. If there are several, will choose the one named "list.csv"
	 * 
	 * @return true if a csv was found, false otherwise.
	 */
	public int findAndSetFile() {
		if (!dirGiven) {
			if (file == null) {
				throw new RuntimeException("No csv file was stored despite isDefault being false");
			}
			return (file.exists()) ? 1 : 0;
		}

		File[] fileArray = directory.listFiles(new CSVFilter());
		if (fileArray == null) {
			return 0;
		}
		List<File> files = new ArrayList<>(Arrays.asList(fileArray));
		if (files.size() == 1) {
			this.file = files.get(0);
		} else {
			for (File possibleFile : files) {
				if (Options.DEFAULT_FILE_NAME.equals(possibleFile.getName())) {
					this.file = possibleFile;
					break;
				}
			}
		}

		if (this.file != null) {
			System.out.println("Assuming that " + file.getPath() + " is the desired file");
			return 1;
		}
		return files.size();

	}

	/** Creates the stored file 
	 * @return true if creation was successful
	 */
	public boolean createFile() {
		try {
			return file.createNewFile();
		} catch (IOException e) {
			System.err.println("Couldn't create new file!");
			e.printStackTrace();
			return false;
		}
	}

	
	/** Creates a file in the directory stored by the handler
	 * @param fileName name of the file to store
	 * @return true if creation successful
	 */
	public boolean createFileInDirectory(String fileName) {
		File newFile = new File(this.directory.getPath() +"/"+ fileName);
		try {
			return newFile.createNewFile();
		} catch (IOException e) {
			System.err.println("Couldn't create new file!");
			e.printStackTrace();
			return false;
		}
	}

	public File getFile() {
		return file;
	}

	public File getDirectory() {
		return directory;
	}

	public boolean isDirGiven() {
		return dirGiven;
	}

}
