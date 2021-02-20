package fun.gbr.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Interface for all classes that write lists to some output
 *
 */
public interface ListWriter {
	
	/** Writes the list to output
	 * @param list
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeList(List<String> list) throws FileNotFoundException, IOException;

}
