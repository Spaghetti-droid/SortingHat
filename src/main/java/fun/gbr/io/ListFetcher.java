package fun.gbr.io;

import java.util.List;

import fun.gbr.ui.UserQuit;

/**
 * Interface for all classes that load a list into the program
 *
 */
public interface ListFetcher {
	
	/** Obtains list from an input
	 * @return list of elements to treat
	 * @throws UserQuit If user quits
	 */
	public List<String> getList() throws UserQuit;

}
