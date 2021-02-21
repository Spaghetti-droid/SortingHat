package fun.gbr.filter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fun.gbr.parameters.Options;

/**
 * Specifies what is recognised as a csv by our standards
 *
 */
public class CSVFilter implements FilenameFilter {
	
	@Override
	public boolean accept(File dir, String name) {
		
		if(name==null || name.isEmpty()) {
			return false;
		}
		int endIdx = name.lastIndexOf(".");
		String ending = name.substring(endIdx+1);
		return Options.CSV_FILE_EXTENSIONS.contains(ending);
	}

}
