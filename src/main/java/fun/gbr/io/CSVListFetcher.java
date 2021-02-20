package fun.gbr.io;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Fetches info from a text file, assuming csv delimiter
 *
 */
public class CSVListFetcher implements ListFetcher {
	
	private FileHandler handler;
	private List<String> csvList;

	public CSVListFetcher(FileHandler handler) throws FileNotFoundException {
		super();
		this.handler = handler;
		init();
	}

	/** Reads the file containing the list, and stores the list
	 * @throws FileNotFoundException
	 */
	private void init() throws FileNotFoundException {
		 csvList = new ArrayList<>();
		Scanner scanner = new Scanner(handler.getFile());
		scanner.useDelimiter(",");
		while(scanner.hasNext()) {
			csvList.add(scanner.next());
		}		
	}
	
	@Override
	public List<String> getList() {
		return this.csvList;
	}

}
