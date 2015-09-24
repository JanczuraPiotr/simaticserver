package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author pjanczura
 */
public class CommandNull extends Command{

	public CommandNull(byte processId, DataInputStream commandInputStream) throws IOException {
		super(processId, commandInputStream);
	}

	@Override
	protected void prepareContent() {

	}

}
