package fun.gbr.ui;

public class QuitScreen implements Screen {

	@Override
	public void open() throws UserQuit {
		throw new UserQuit();
	}
}
