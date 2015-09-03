package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class BramaWriteDBByte extends Command{

	public BramaWriteDBByte(DataInputStream commandInputStream) throws IOException {
		super(commandInputStream);
	}

	@Override
	protected void prepareContent() {
	}

}
