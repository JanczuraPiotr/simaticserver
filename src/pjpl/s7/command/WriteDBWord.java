package pjpl.s7.command;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class WriteDBWord extends Command{

	public WriteDBWord(byte processId, DataInputStream commandInputStream) throws IOException {
		super(processId, commandInputStream);
	}

	@Override
	protected void prepareContent() {
	}

}
