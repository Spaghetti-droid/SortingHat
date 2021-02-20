package fun.gbr.filter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Specifies what is recognised as a csv by our standards
 *
 */
public class CSVFilter implements FilenameFilter {

	private final Set<String> allowedTypes = new HashSet<>(Arrays.asList("csv", "txt"));
	
	@Override
	public boolean accept(File dir, String name) {
		
		if(name==null || name.isBlank()) {
			return false;
		}
		int endIdx = name.lastIndexOf(".");
		String ending = name.substring(endIdx+1);
		return allowedTypes.contains(ending);
	}

}
