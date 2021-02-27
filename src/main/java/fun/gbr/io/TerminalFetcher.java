package fun.gbr.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import fun.gbr.ui.UIUtils;
import fun.gbr.ui.UserQuit;

import static fun.gbr.ui.UIUtils.treatUserInput;

/**
 * For fetching data from terminal input
 *
 */
public class TerminalFetcher implements ListFetcher {

	private Scanner scanner;
	private List<String> userList;

	public TerminalFetcher(Scanner scanner) {
		super();
		this.scanner = scanner;
	}

	@Override
	public List<String> getList() throws UserQuit {

		// Ask for new list if needed

		String prompt = "Input the list of elements to shuffle, seperated by ','";
		if (this.userList != null) {
			prompt += ". Enter to re-use the previous list";
		}
		prompt+=":";

		String terminalInput = treatUserInput(scanner, prompt);
		if(!terminalInput.isEmpty()) {
			this.userList = new ArrayList<>(Arrays.asList(terminalInput.split(",")));
		} 

		return this.userList;
	}

	@Override
	public boolean onInvalidList() throws UserQuit {

		String response = UIUtils.treatUserInput(scanner, "Would you like to try again?(y/n)");
		if ("y".equals(response)) {
			return true;
		}
		return false;
	}

}
