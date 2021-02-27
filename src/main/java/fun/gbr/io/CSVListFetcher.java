package fun.gbr.io;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

	public CSVListFetcher(Scanner scanner, FileHandler handler) throws FileNotFoundException {
		super();
		this.handler = handler;
		this.scanner = scanner;
		init();
	}

	/** Reads the file containing the list, and stores the list
	 * @throws FileNotFoundException
	 */
	private void init() throws FileNotFoundException {
		 csvList = new ArrayList<>();
		Scanner fileScanner = new Scanner(handler.getFile());
		fileScanner.useDelimiter(",");
		while(fileScanner.hasNext()) {
			csvList.add(fileScanner.next());
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
			}
			return true;
		}
		return false;
	}
}
