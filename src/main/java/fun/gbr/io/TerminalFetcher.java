package fun.gbr.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import fun.gbr.ui.UserQuit;

import static fun.gbr.ui.UIUtils.treatUserInput;

public class TerminalFetcher implements ListFetcher {
	
	private Scanner scanner;	
	private List<String> userList;

	public TerminalFetcher(Scanner scanner) {
		super();
		this.scanner = scanner;
	}

	@Override
	public List<String> getList() throws UserQuit {

		if(this.userList != null) {
			String choice = treatUserInput(scanner, "Re-shuffle previous list?(y/n)");
			if(!"y".equals(choice)) {
				userList = null;
			}
		}
		
		if(this.userList == null) {
			
			String terminalInput = treatUserInput(scanner, "Input the list of elements to shuffle, seperated by ',': ");

			this.userList = new ArrayList<String>(Arrays.asList(terminalInput.split(",")));
		}
		
		return this.userList;
		
	}

}
