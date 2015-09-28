package pjpl.s7.command;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public class CommandNull extends Command{

	public CommandNull(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	protected void loadParameters() {

	}

	@Override
	public CommandResponse action(pjpl.s7.process.Process process) {
		try {
			System.err.println("CommandNull");
			return new ResponseNo(processId, getCommandCode(), socket);
		} catch (IOException ex) {
			Logger.getLogger(CommandNull.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public short getCommandCode() {
		return 0;
	}

}
