package fun.gbr.io;

import java.util.List;

/**
 * For outputting results to the terminal
 *
 */
public class TerminalWriter implements ListWriter {

	@Override
	public void writeList(List<String> elements) {
		
		System.out.println("Shuffled list: " + elements);

	}

}
