package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class WriteDBByte extends Command{

	public WriteDBByte(byte processId, DataInputStream commandInputStream) throws IOException {
		super(processId, commandInputStream);
	}
	@Override
	protected void prepareContent() {
	}

}
