package fun.gbr.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;

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
	
	public String escapeSpecialCharacters(String element) {
		
	    String escapedData = element.replaceAll("\\R", " ");
	    if (element.contains(",") || element.contains("\"") || element.contains("'")) {
	        element = element.replace("\"", "\"\"");
	        escapedData = "\"" + element + "\"";
	    }
	    return escapedData;
		
	}

}
