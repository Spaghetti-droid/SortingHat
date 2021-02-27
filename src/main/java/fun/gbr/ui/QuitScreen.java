package fun.gbr.ui;

import java.util.Scanner;

/**
 * Screen for handling events that transpire after the user has decided to quit.
 *
 */
public class QuitScreen implements Screen {

	@Override
	public void open(Scanner scanner) throws UserQuit {
		throw new UserQuit();
	}
}
