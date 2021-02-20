package fun.gbr.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;

/**
 * Outputs list to a CSV
 *
 */
public class CSVListWriter implements ListWriter {

	private FileHandler handler;
	
	public CSVListWriter(FileHandler handler) {
		super();
		this.handler = handler;
	}

	@Override
	public void writeList(List<String> list) throws IOException {
		
		String[] line = new String[list.size()];
		list.toArray(line);
		
		try(CSVWriter writer = new CSVWriter(new FileWriter(handler.getFile(), true))){
			writer.writeNext(line);
		}

		System.out.println("Done.");		
	}
}
