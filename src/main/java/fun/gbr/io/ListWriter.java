package fun.gbr.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ListWriter {
	
	public void writeList(List<String> list) throws FileNotFoundException, IOException;

}
