package fun.gbr.io;

import java.util.List;

import fun.gbr.ui.UserQuit;

public interface ListFetcher {
	
	public List<String> getList() throws UserQuit;

}
