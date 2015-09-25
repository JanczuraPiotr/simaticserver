package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author pjanczura
 */
public class CommandNull extends Command{

	public CommandNull(byte processId, DataInputStream commandInputStream, OutputStream outputStream) throws IOException {
		super(processId, commandInputStream, outputStream);
	}

	@Override
	protected void loadParameters() {

	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		System.err.println("CommandNull");
		return new ResponseNo(processId, outputStream);
	}

	@Override
	public short getCommandCode() {
		return 0;
	}

}
