package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author pjanczura
 */
public class CommandNull extends Command{

	public CommandNull(DataInputStream commandInputStream) throws IOException {
		super(commandInputStream);
	}

	@Override
	protected void prepareContent() {

	}

}