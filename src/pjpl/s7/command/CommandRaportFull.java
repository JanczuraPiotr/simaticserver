package pjpl.s7.command;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.s7.process.Process;
import pjpl.s7.common.CommandCode;
import pjpl.s7.util.MemByteClip;
//import pjpl.s7.command.CommandRaportFull;

/**
 *
 * @author pjanczura
 */
public class CommandRaportFull extends Command{

	public CommandRaportFull(byte processId, Socket socket) throws IOException {
		super(processId, socket);
	}

	@Override
	public short getCommandCode() {
		return CommandCode.RAPORT_FULL;
	}

	@Override
	public CommandResponse action(Process process) {
		try {
			MemByteClip raport = process.getRaport();
			return new ResponseRaportFull(getProcessId(), getCommandCode(),raport, socket);
		} catch (IOException ex) {
			Logger.getLogger(CommandRaportFull.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	protected void loadParameters() {
	}
}
