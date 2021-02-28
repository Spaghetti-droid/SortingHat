package fun.gbr.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import fun.gbr.ui.UIUtils;
import fun.gbr.ui.UserQuit;

/**
 * Fetches info from a text file, assuming csv delimiter
 *
 */
public class CSVListFetcher implements ListFetcher {
	
	private FileHandler handler;
	private Scanner scanner;
	private List<String> csvList;

	public CSVListFetcher(Scanner scanner, FileHandler handler) throws IOException, CsvException {
		super();
		this.handler = handler;
		this.scanner = scanner;
		init();
	}

	/** Reads the file containing the list, and stores the list
	 * @throws IOException 
	 * @throws CsvException 
	 */
	private void init() throws IOException, CsvException {
		 csvList = new ArrayList<>();
		 CSVReader reader = new CSVReader(Files.newBufferedReader(handler.getFile().toPath()));		
		 List<String[]> lines = reader.readAll();
		 for(String[] line:lines) {
			 csvList.addAll(Arrays.asList(line));
		 }		 
	}
	
	@Override
	public List<String> getList() {
		return this.csvList;
	}

	@Override
	public boolean onInvalidList() throws UserQuit {
		
		System.out.println("In order to obtain a list that can be shuffled, you may modify the file and save it. Alternatively you may modify the MAX_REPEATS option with 'o'.");
		String response = UIUtils.treatUserInput(scanner, "Would you like to try again?(y/n)");
		if("y".equals(response)) {
			try {
				init();
			} catch (FileNotFoundException e) {
				System.err.println("Couldn't find file! Has it been moved?");
				return false;
			} catch (IOException e) {
				System.err.println("Attempt to read file failed!");
				e.printStackTrace();
			} catch (CsvException e) {
				System.err.println("Error treating csv information!");
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
