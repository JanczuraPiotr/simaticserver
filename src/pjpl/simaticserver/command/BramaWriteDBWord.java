package pjpl.simaticserver.command;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class BramaWriteDBWord extends Command{

	public BramaWriteDBWord(DataInputStream commandInputStream) throws IOException {
		super(commandInputStream);
	}

	@Override
	protected void prepareVariable() {
	}

}
